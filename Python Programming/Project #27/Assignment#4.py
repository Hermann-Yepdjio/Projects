import pandas as pd

def read_data():
    AAPL_DF = pd.read_csv('Data/AAPL.csv') #read the AAPL CSV file 
    AMZN_DF = pd.read_csv('Data/AMZN.csv') #read the AMZN CSV file
    FB_DF = pd.read_csv('Data/FB.csv') #read the FB CSV file
    GOOG_DF = pd.read_csv('Data/GOOG.csv') #read the GOOG CSV file
    IBM_DF = pd.read_csv('Data/IBM.csv') #read the IBM CSV file
    MSFT_DF = pd.read_csv('Data/MSFT.csv') #read the MSFT CSV file
    return (AAPL_DF, AMZN_DF, FB_DF, GOOG_DF, IBM_DF, MSFT_DF)

def compute_dividend():
    AAPL_DF, AMZN_DF, FB_DF, GOOG_DF, IBM_DF, MSFT_DF = read_data()
    
    #compute dividend for AAPL_DF
    print("---------------------------------------------Start computing dividends for AAPL.CSV-----------------------------------------------")
    AAPL_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for AAPL.CSV
    for i in range (1, len(AAPL_DF), 1):
        Close_ratio = AAPL_DF['Close'][i - 1] / AAPL_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = AAPL_DF['Adj Close'][i - 1] / AAPL_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0): 
            AAPL_DF_Final = AAPL_DF_Final.append({'Date': AAPL_DF['Date'][i], 'Dividend': diff * AAPL_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(AAPL_DF['Date'][i])+" the dividend was: $"+str(diff * AAPL_DF['Close'][i])+"." )
        
    print("---------------------------------------------End computing dividends for AAPL.CSV-----------------------------------------------")

    #compute dividend for AMZN_DF
    print("---------------------------------------------Start computing dividends for AMZN.CSV-----------------------------------------------")
    AMZN_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for AMZN.CSV
    for i in range (1, len(AMZN_DF), 1):
        Close_ratio = AMZN_DF['Close'][i - 1] / AMZN_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = AMZN_DF['Adj Close'][i - 1] / AMZN_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0):
            AMZN_DF_Final = AMZN_DF_Final.append({'Date': AMZN_DF['Date'][i], 'Dividend': diff * AMZN_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(AMZN_DF['Date'][i])+" the dividend was: $"+str(diff * AMZN_DF['Close'][i])+"." )
        
    print("---------------------------------------------End computing dividends for AMZN.CSV-----------------------------------------------")

    #compute dividend for FB_DF 
    print("---------------------------------------------Start computing dividends for FB.CSV-----------------------------------------------")
    FB_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for FB.CSV
    for i in range (1, len(FB_DF), 1):
        Close_ratio = FB_DF['Close'][i - 1] / FB_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = FB_DF['Adj Close'][i - 1] / FB_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0):
            FB_DF_Final = FB_DF_Final.append({'Date': FB_DF['Date'][i], 'Dividend': diff * FB_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(FB_DF['Date'][i])+" the dividend was: $"+str(diff * FB_DF['Close'][i])+".")
        
    print("---------------------------------------------End computing dividends for FB.CSV-----------------------------------------------")

    #compute dividend for GOOG_DF
    print("---------------------------------------------Start computing dividends for GOOG.CSV-----------------------------------------------")
    GOOG_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for GOOG.CSV
    for i in range (1, len(GOOG_DF), 1):
        Close_ratio = GOOG_DF['Close'][i - 1] / GOOG_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = GOOG_DF['Adj Close'][i - 1] / GOOG_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0):
            GOOG_DF_Final = GOOG_DF_Final.append({'Date': GOOOG_DF['Date'][i], 'Dividend': diff * GOOG_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(GOOG_DF['Date'][i])+" the dividend was: $"+str(diff * GOOG_DF['Close'][i])+".")
        
    print("---------------------------------------------End computing dividends for GOOG.CSV-----------------------------------------------")

    #compute dividend for IBM_DF
    print("---------------------------------------------Start computing dividends for IBM.CSV-----------------------------------------------")
    IBM_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for IBM.CSV
    for i in range (1, len(IBM_DF), 1):
        Close_ratio = IBM_DF['Close'][i - 1] / IBM_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = IBM_DF['Adj Close'][i - 1] / IBM_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0):
            IBM_DF_Final = IBM_DF_Final.append({'Date': IBM_DF['Date'][i], 'Dividend': diff * IBM_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(IBM_DF['Date'][i])+" the dividend was: $"+str(diff * IBM_DF['Close'][i])+".")
        
    print("---------------------------------------------End computing dividends for IBM.CSV-----------------------------------------------")

    #compute dividend for MSFT_DF
    print("---------------------------------------------Start computing dividends for MSFT.CSV-----------------------------------------------")
    MSFT_DF_Final = pd.DataFrame(columns=['Date', 'Dividend']) #to hold final results (Data + Dividends) for MSFT.CSV
    for i in range (1, len(MSFT_DF), 1):
        Close_ratio = MSFT_DF['Close'][i - 1] / MSFT_DF['Close'][i] # compute the ratio of previous day's "Close" price and today's "Close" price
        Adj_Close_ratio = MSFT_DF['Adj Close'][i - 1] / MSFT_DF['Adj Close'][i] # compute the ratio of previous day's "Adj Close" price and today's "Adj Close" price
        diff = abs(Close_ratio - Adj_Close_ratio)

        if(diff != 0):
            MSFT_DF_Final = MSFT_DF_Final.append({'Date': MSFT_DF['Date'][i], 'Dividend': diff * MSFT_DF['Close'][i]}, ignore_index=True)
        print("On day "+str(MSFT_DF['Date'][i])+" the dividend was: $"+str(diff * MSFT_DF['Close'][i])+"." )
        
    print("---------------------------------------------End computing dividends for MSFT.CSV-----------------------------------------------")

    print("Final AAPL Dataframe:\n", AAPL_DF_Final, "\n\n")
    print("Final AMZN Dataframe:\n", AMZN_DF_Final, "\n\n")
    print("Final FB Dataframe:\n", FB_DF_Final, "\n\n")
    print("Final GOOG Dataframe:\n", GOOG_DF_Final, "\n\n")
    print("Final IBM Dataframe:\n", IBM_DF_Final, "\n\n")
    print("Final MSFT Dataframe:\n", MSFT_DF_Final, "\n\n")


def main():
    pd.set_option("display.max_rows", None, "display.max_columns", None)
    compute_dividend()

if __name__ == "__main__":
    main()
