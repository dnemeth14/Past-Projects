import getTodaysGames
import calculateWinProbability
import scrapeData
import displayOutput
import game

if __name__ == "__main__":

    whichModel = int(input("Which model do you want to run, OG (enter 0) or ML (enter 1): "))

    # Case where ML is chosen
    if(whichModel):
        # creates a list of Game objects for each game today
        todaysGames = getTodaysGames.getGames()

        # iterate through the list of games, get Data and print out what moneylines to bet them at
        for x in range(len(todaysGames)):
            try:
                todaysGames[x].getMLData()
                print(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData)
                winProbabilities = calculateWinProbability.predictWinProbabilities(todaysGames[x].roadTeamData,
                                                                                   todaysGames[x].homeTeamData)
                displayOutput.display(todaysGames[x].roadTeam, todaysGames[x].homeTeam,
                                      winProbabilities, todaysGames[x].roadTeamData['pitcherGamesPlayed'],
                                      todaysGames[x].homeTeamData['pitcherGamesPlayed'])
            except:
                print(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData)
                print("error for game: " + todaysGames[x].homeTeam + " vs. " + todaysGames[x].roadTeam)

    # Case where OG is run
    else:
        #creates a list of Game objects for each game today
        todaysGames = getTodaysGames.getGames()
        averageHardHitRate = scrapeData.getAverageHardHitRate()

        #iterate through the list of games, get Data and print out what moneylines to bet them at
        for x in range(len(todaysGames)):
            todaysGames[x].getOGData()
            print(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData)
            winProbabilities = calculateWinProbability.calculateWinProbabilities(todaysGames[x].roadTeamData,
                                                                                 todaysGames[x].homeTeamData,
                                                                                 averageHardHitRate)
            displayOutput.display(todaysGames[x].roadTeam, todaysGames[x].homeTeam, winProbabilities,
                                            todaysGames[x].roadTeamData['pitcherGamesPlayed'],
                                      todaysGames[x].homeTeamData['pitcherGamesPlayed'])
            '''
            try:
                todaysGames[x].getOGData()
                print(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData)
                winProbabilities = calculateWinProbability.calculateWinProbabilities(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData, averageHardHitRate)
                displayOutput.display(todaysGames[x].roadTeam, todaysGames[x].homeTeam, winProbabilities)
            except:
                print(todaysGames[x].roadTeamData, todaysGames[x].homeTeamData)
                print("error for game: " + todaysGames[x].homeTeam + " vs. " + todaysGames[x].roadTeam)
            '''



