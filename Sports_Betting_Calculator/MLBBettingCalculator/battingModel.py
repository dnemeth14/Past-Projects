import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from functools import reduce
import sklearn
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
import pickle

# Read in all the files for a single season and merge them into a single data frame
def read(year) -> object:
    # Read in all the files
    # Change the path when you run it on your machine
    path = "C:\\Users\\19258\\Downloads\\"
    batting = pd.read_csv(path + "WUS" + str(year) + "Batting.csv")

    # Merge the files
    return batting

# Reading in the training and testing data
data = pd.concat([read(2019), read(2018), read(2017), read(2015), read(2014)])
predict_data = read(2016)

# Create training labels
data_label = data["RBI"]/162
predict_data_label = predict_data["RBI"]/162

# Averaging out columns
data["HR"] /= 162; data["SB"] /= 162
predict_data["HR"] /= 162; predict_data["SB"] /= 162

# Drop edge case rows
data.drop(columns = ["Team", "R", "RBI", "G", "PA", "Off", "Def", "WAR"], inplace = True)
predict_data.drop(columns = ["Team", "R", "RBI", "G", "PA", "Off", "Def", "WAR"], inplace = True)

# Replacing duplicate feature names
cols = pd.Series(data.columns)

for dup in cols[cols.duplicated()].unique():
    cols[cols[cols == dup].index.values.tolist()] = [dup + '.' + str(i) if i != 0 else dup for i in range(sum(cols == dup))]

# rename the columns with the cols list.
data.columns = cols
predict_data.columns = cols

# Converting % data into float
for col in data:
    if("%" in col):
        data[col] = data[col].str.rstrip('%').astype('float') / 100.0
        predict_data[col] = predict_data[col].str.rstrip('%').astype('float') / 100.0

# Change data type of the dataframe so it can be trained on
data = data.astype("float")
predict_data = predict_data.astype("float")

from sklearn.ensemble import GradientBoostingRegressor as GBR
prediction_model = GBR(n_estimators = 1500, learning_rate = 0.05,
                n_iter_no_change = 20, validation_fraction = 0.15)
prediction_model.fit(data, data_label)

# Testing the model
prediction = prediction_model.predict(predict_data)

# Evaluating the model
mae = mean_absolute_error(prediction, predict_data_label)
mse = mean_squared_error(prediction, predict_data_label)
print(" Mean absolute error:", mae, '\n', "Mean squared error:", mse)

# Save the model to disk
filename = "wet_unseld_secret_sauce.sav"
pickle.dump(prediction_model, open(filename, 'wb'))