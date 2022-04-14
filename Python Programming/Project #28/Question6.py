

import pandas as pd
import matplotlib.pyplot as plt
import math

def low(current):
   bar = current.quantile(q=0.5)
   l = current.tolist()
   for j in range(len(l)):
                if l[j] > bar:
                    current = current.drop(j)
   return current
        
def high(current):
   bar = current.quantile(q=0.5)
   l = current.tolist()
   for j in range(len(l)):
                if l[j] < bar:
                    current = current.drop(j)
   return current
        
def plan(df, interval, plan):
    money = 5000000
    mtm = pd.Series([])
    shares = pd.Series([])
    i = 0
    last = pd.Series([])
    current = pd.Series([])
    choice = pd.Series([])
    done = False
    while not(done):
        if i == 0:
            current = df.loc[df.index[0]]
            choice = pd.Series([current[0], current[2], current[4], current[6], current[8]])
            mtm[df.index[0]] = money
            money = money/5
            shares = pd.Series([math.floor(money/choice[0]), math.floor(money/choice[1]), math.floor(money/choice[2]), math.floor(money/choice[3]), math.floor(money/choice[4])])
            money = 5 * money - shares[0] * choice[0] - shares[1] * choice[1] - shares[2] * choice[2] - shares[3] * choice[3] - shares[4] * choice[4]
        else:
            last = current
            if i < 250:
                current = df.loc[df.index[i]].tolist()
            else:
                i = 250
                current = df.loc[df.index[i]].tolist()
                done = True
            
          
            money = money + shares[0] * current[choice.index[0] * 2] + shares[1] * current[choice.index[1] * 2] + shares[2] * current[choice.index[2] * 2] + shares[3] * current[choice.index[3] * 2] + shares[4] * current[choice.index[4] * 2]
            mtm[df.index[i]] = money
            prices = pd.Series([current[1]/last[1], current[3]/last[3],
                              current[5]/last[5], current[7]/last[7],
                              current[9]/last[9], current[11]/last[11], 
                              current[13]/last[13], current[15]/last[15],
                              current[17]/last[17], current[19]/last[19]])
            if plan == "H":
                choice = high(prices)
            elif plan == "L":
                choice = low(prices)
            
            s = pd.Series([current[choice.index[0] * 2], current[choice.index[1] * 2], current[choice.index[2] * 2], current[choice.index[3] * 2], current[choice.index[4] * 2]])
            money = money / 5
            shares = pd.Series([math.floor(money/s[0]), math.floor(money/s[1]), math.floor(money/s[2]), math.floor(money/s[3]), math.floor(money/s[4])])
            money = 5 * money - shares[0] * s[0] - shares[1] * s[1] - shares[2] * s[2] - shares[3] * s[3] - shares[4] * s[4]
        
        i = i + interval
            
    return mtm\
        
def plotMTM(buy_high, buy_low):
    df1 = pd.DataFrame(buy_high.tolist())
    df1.index = buy_high.index
    df2 = pd.DataFrame(buy_low.tolist())
    df2.index = buy_low.index
    
    fig, ax = plt.subplots(1, 1, figsize=(12, 4))
    df1.plot(ax=ax)
    df2.plot(ax=ax)
    ax.legend(['high', 'low'])
    ax.set_title("MTM")
    ax.set_xlabel("Dates")
    ax.set_ylabel("Dollars per Million")
    
    return

def plotOptimalMTM(df):
    interval_buy_high = pd.Series([])
    interval_buy_low = pd.Series([])
    for i in range(251):
        if i > 0:
            high = plan(df, i, "H")
            interval_buy_high[i] = high['2018-12-31']
            low = plan(df, i, "L")
            interval_buy_low[i] = low['2018-12-31']
            
    df1 = pd.DataFrame(interval_buy_high.tolist())
    df1.index = interval_buy_high.index
    df2 = pd.DataFrame(interval_buy_low.tolist())
    df2.index = interval_buy_low.index
    
    fig, ax = plt.subplots(1, 1, figsize=(12, 4))
    df1.plot(ax=ax)
    df2.plot(ax=ax)
    ax.legend(['high', 'low'])
    ax.set_title("MTM intervals of 2018")
    ax.set_xlabel("intervals")
    ax.set_ylabel("Dollars per Million")
    
    optimal_interval_buy_low = interval_buy_low.idxmax()
    optimal_interval_buy_high = interval_buy_high.idxmax()
    optimal_MTM_buy_low = interval_buy_low[optimal_interval_buy_low]
    optimal_MTM_buy_high = interval_buy_high[optimal_interval_buy_high]
    
    
    print("optimal_interval_buy_low = " + str(optimal_interval_buy_low))
    print("optimal_interval_buy_high = " + str(optimal_interval_buy_high))
    print("optimal_MTM_buy_low = " + str(optimal_MTM_buy_low))
    print("optimal_MTM_buy_high = " + str(optimal_MTM_buy_high))
    
    return

def main():
    list = ['IBM', 'MSFT', 'GOOG', 'AAPL', 'AMZN', 'FB', 'NFLX', 'TSLA', 'ORCL', 'SAP'] 
    data = pd.read_csv('IBM.csv')
    df = pd.DataFrame(data)
    df = df[["Date"]]
    df = df.set_index("Date")
    
    for i in list:
        data = pd.read_csv(i + '.csv')
        df2 = pd.DataFrame(data)
        df.loc[:,i + ' Close'] = df2['Close'].tolist()
        df.loc[:,i + ' Adj Close'] = df2['Adj Close'].tolist()
    
    buy_high = plan(df, 5, "H")
    buy_low = plan(df, 5, "L")
    plotMTM(buy_high, buy_low)

    plotOptimalMTM(df)
    print("MTM_Buy_Low = " + str(buy_low['2018-12-31']))
    print("MTM_Buy_High = " + str(buy_high['2018-12-31']))
    plt.show()
    
main()
        

  
