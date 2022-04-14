import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from numpy import inf

def read_data():

    #open all csv files and load data into pandas dataframes
    df_IBM = pd.read_csv ('IBM.csv')
    df_MSFT = pd.read_csv ('MSFT.csv')
    df_GOOG = pd.read_csv ('GOOG.csv')
    df_AAPL = pd.read_csv ('AAPL.csv')
    df_AMZN = pd.read_csv ('AMZN.csv')
    df_FB = pd.read_csv ('FB.csv')
    df_NFLX = pd.read_csv ('NFLX.csv')
    df_TSLA = pd.read_csv ('TSLA.csv')
    df_ORCL = pd.read_csv ('ORCL.csv')
    df_SAP = pd.read_csv ('SAP.csv')

    #extract "Close" and "Adj Close" columns from each dataframe and create a new dataframe with those columns
    data = [df_IBM[["Close", "Adj Close"]], df_MSFT[["Close", "Adj Close"]], df_GOOG[["Close", "Adj Close"]], df_AAPL[["Close", "Adj Close"]], df_AMZN[["Close", "Adj Close"]], df_FB[["Close", "Adj Close"]], df_NFLX[["Close", "Adj Close"]], df_TSLA[["Close", "Adj Close"]], df_ORCL[["Close", "Adj Close"]], df_SAP[["Close", "Adj Close"]]]
    universe = pd.concat(data, axis=1)
#    print(universe.to_numpy()[3][4])

    return universe

########################################################################
# Compute MTM for every business day                                   #
# universe is the main data frame with 20 columns                      # 
# shares a dataframe. Contains number of shares, col_index (in universe# 
# of corresponding stock's closePrice and amount of cash left          #
# after purchasing shares (this is in the last column)                 #
########################################################################
def MTM(universe, shares, num_stock_to_buy, interval):
#    mtm = cash
    mtms = pd.Series(0, index=range(len(universe.index))) #To hold the mtms for each business day
    for i in universe.index: #compute num_shares * closePrice for all the shares we own
        tmp_mtm = 0
        for j in range(num_stock_to_buy):
            tmp_mtm += shares.at[i // interval, j * 2] * universe.at[i, shares.at[i // interval, j * 2 + 1]] # compute num_shares * closePrice of day i and add it to tmp_mtm
        tmp_mtm += shares.at[i // interval, num_stock_to_buy * 2] # add the cash remaining in the account
        mtms.loc[i] = tmp_mtm
    return mtms

def compute_percentage_changes(universe, interval):
    tmp = universe.drop (universe.columns[[i for i in universe.columns if i % 2 == 0]], axis=1, inplace=False) #drop the closePrice columns
    tmp = tmp.pct_change(periods=1) #compute the percentage changes after each day
    tmp = tmp.iloc[1:len(tmp.index) - interval + 1]
    tmp = tmp.reset_index(drop = True)
    tmp = tmp.groupby(tmp.index // interval).sum()
    return tmp

########################################################################
# Purchase stocks                                                      #
# buy_low is a boolean. True->buy low, False->Buy high                 #
########################################################################
def purchase_stocks(universe, buy_low,  p_changes, interval, cash, num_stock_to_buy):
    current_cash = 0 # to keep track of how much cash I have
    cash_per_stock = cash/num_stock_to_buy # how much money I can spend on a given stock 
    shares = pd.DataFrame(0, index=range(len(p_changes.index) + 1), columns=range(num_stock_to_buy * 2 + 1)) #To store info about purchased stocks and my cash account (last column)
    shares[num_stock_to_buy * 2] = shares[num_stock_to_buy * 2].astype(float)
    for i in range(num_stock_to_buy): #On day #1, we always buy the first 5 (or num_stock_to_buy) stocks
        shares.at[0, i * 2] = cash_per_stock // universe.at[0, universe.columns[i * 2]] # store how many shares we purchased for the corresponding stock
        shares.at[0, i * 2 + 1] = universe.columns[i * 2] # store col_index of closeprice next to the number of shares
        shares.at[0, num_stock_to_buy * 2] += float(cash_per_stock % universe.at[0, universe.columns[i * 2]]) #update the amount of cash we have (stored in the last column)
    for i in p_changes.index: # buy stocks every 5 or (interval) days
        current_cash = shares.at[i, num_stock_to_buy * 2] 
        for j in range(num_stock_to_buy): # sell all the shares
            current_cash += shares.at[i, j * 2] * universe.at[(i + 1) * interval, shares.at[i, j * 2 + 1]]

        cash_per_stock = current_cash/num_stock_to_buy # compute new amount of cash to spend on each stock 
        tmp = p_changes.loc[[i]] # select a row
        tmp = tmp.sort_values(by=i, ascending = buy_low, axis=1) #sort the selected row to select the first 5 stocks 
        for j in range(num_stock_to_buy): # by the 5 (or num_stock_to_buy) stocks with lowest or highest adj close price drop
            shares.at[i + 1, j * 2] = cash_per_stock // universe.at[(i + 1) * interval, tmp.columns[j]] # store how many shares we purchased for the corresponding stock
            shares.at[i + 1, j * 2 + 1] = tmp.columns[j] # store col_index of closeprice next to the number of shares
            shares.at[i + 1, num_stock_to_buy * 2] += cash_per_stock % universe.at[(i + 1) * interval, tmp.columns[j]] #update the amount of cash we have (stored in the last column)

    return shares


def factors(n):
    return set(
        factor for i in range(1, int(n**0.5) + 1) if n % i == 0
        for factor in (i, n//i)
    )

def try_all_possible_intervals(universe, cash, num_stock_to_buy):
    pos_intervals = factors(len(universe.index))
    optimal_interval_buy_low, optimal_mtm_buy_low, optimal_interval_buy_high, optimal_mtm_buy_high = float(-inf), float(-inf), float(-inf), float(-inf)
    for interval in pos_intervals:
        p_changes = compute_percentage_changes(universe, interval)
        shares_buy_low = purchase_stocks(universe, True,  p_changes, interval, cash, num_stock_to_buy)
        mtms_buy_low = MTM(universe, shares_buy_low, num_stock_to_buy, interval)
        shares_buy_high = purchase_stocks(universe, False,  p_changes, interval, cash, num_stock_to_buy)
        mtms_buy_high = MTM(universe, shares_buy_high, num_stock_to_buy, interval)
        (optimal_interval_buy_low, optimal_mtm_buy_low) = (interval, mtms_buy_low.loc[len(universe.index) - 1]) if mtms_buy_low.loc[len(universe.index) - 1] > optimal_mtm_buy_low else (optimal_interval_buy_low, optimal_mtm_buy_low) # check if new optimal_mtm_buy_low is greater than the old one and update if necessary
        (optimal_interval_buy_high, optimal_mtm_buy_high) = (interval, mtms_buy_high.loc[len(universe.index) - 1]) if mtms_buy_high.loc[len(universe.index) - 1] > optimal_mtm_buy_high else (optimal_interval_buy_high, optimal_mtm_buy_high) # check if new optimal_mtm_buy_high is greater than the old one and update if necessary
        plt.plot(mtms_buy_low.index, mtms_buy_low.values) # Plot mtm_buy_low
        plt.plot(mtms_buy_high.index, mtms_buy_high.values) # plot mtm_buy_high
    print("optimal_interval_buy_low: ", optimal_interval_buy_low, ".     optimal_MTM_buy_low: ", optimal_mtm_buy_low, ".\noptimal_interval_buy_high: ", optimal_interval_buy_high, ".     optimal_MTM_buy_high: ", optimal_mtm_buy_high, ".\n\n")

def main():
    interval, initial_cash, num_stock_to_buy = 5, 5000000, 5
    universe = read_data()
    universe = universe.T.reset_index(drop=True).T #reset column names to column indexes
    p_changes = compute_percentage_changes(universe, interval)
    shares_buy_low = purchase_stocks(universe, True,  p_changes, interval, initial_cash, num_stock_to_buy)
    mtms_buy_low = MTM(universe, shares_buy_low, num_stock_to_buy, interval)
    shares_buy_high = purchase_stocks(universe, False,  p_changes, interval, initial_cash, num_stock_to_buy)
    mtms_buy_high = MTM(universe, shares_buy_high, num_stock_to_buy, interval)
    print("MTM_buy_low: ", mtms_buy_low.loc[len(universe.index) - 1], "\n\n")
    plt.plot(mtms_buy_low.index, mtms_buy_low.values) # Plot the daily MTM for the entire year for buy low
    plt.show()
    plt.clf()
    print("MTM_buy_high: ", mtms_buy_high.loc[len(universe.index) - 1], "\n\n")
    plt.plot(mtms_buy_high.index, mtms_buy_high.values) # Plot the daily MTM for the entire year for buy high
    plt.show()
    plt.clf()
#    print(shares, "\n\n",mtms)
#    plt.plot(mtms.index, mtms.values)
    try_all_possible_intervals(universe, 5000000, 5) #Try all possible intervals
    plt.show()
#    print(pd.Series(0, index=range(250))[225])
#    df = pd.DataFrame(0, index=range(20), columns=range(0, 20, 2))
#    print(df)
#    purchase_stocks(universe, True, p_changes, 5000000 )
#    print(factors(250))

if __name__ == "__main__" :
    main()
