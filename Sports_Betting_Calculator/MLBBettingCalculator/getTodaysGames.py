import datetime

import requests
from bs4 import BeautifulSoup
import game

todaysGames = []
currentSeason = datetime.datetime.now().strftime("%Y")

def formatName(pitcherName: str) -> object:
    # Convert to lowercase and change spaces into hyphens
    return pitcherName.lower().replace(" ", "-")

def extractNum(pitcherURL: str) -> object:
    # Split the string until only the integer part remains
    return pitcherURL.split("=")[1].split("&")[0]

def getGames():
    URL = 'https://www.fangraphs.com/livescoreboard.aspx'
    page = requests.get(URL)
    soup = BeautifulSoup(page.content, 'html.parser')

    results = soup.find(id='LiveBoard1_LiveBoard1_litGamesPanel')

    fangraphsBaseURL = 'https://www.fangraphs.com/'

    games = results.find_all("b")
    pitchers = results.find_all('table', class_="lineup")

    for x in range(2, len(games), 2):
        # Get team names
        roadTeamName = games[x].text
        homeTeamName = games[x + 1].text

        # Get pitcher names
        index = int((x - 2) / 2)
        roadPitcher = pitchers[index].text.split("SP: ")[1].strip().split("OP")[0]
        homePitcher = pitchers[index].text.split("SP: ")[2].strip().split("1")[0].split("OP")[0]

        # Get URLs
        try:
            roadIndex = pitchers[index].find_all('a')[0]['href']
            homeIndex = pitchers[index].find_all('a')[1]['href']

            roadPitcherURL = str(fangraphsBaseURL + roadIndex)
            homePitcherURL = str(fangraphsBaseURL + homeIndex)

            roadNum = extractNum(roadIndex)
            homeNum = extractNum(homeIndex)

            roadSplitURL = str(fangraphsBaseURL + "players/" + formatName(roadPitcher) + "/" + roadNum + "/splits?position=P&season=" + currentSeason)
            homeSplitURL = str(fangraphsBaseURL + "players/" + formatName(homePitcher) + "/" + homeNum + "/splits?position=P&season=" + currentSeason)

            # print(homeTeamName + ": " + homePitcher + " " + homePitcherURL)
            # print(roadTeamName + ": " + roadPitcher + " " + roadPitcherURL)
            # print(homePitcher)
            # print(homeTeam.getName())

            # create a Game instance for this game and add it to the games list
            thisGame = game.Game(roadTeamName, roadPitcher, roadPitcherURL, roadSplitURL, homeTeamName, homePitcher, homePitcherURL, homeSplitURL)
            todaysGames.append(thisGame)

        except:
            pass

    return todaysGames