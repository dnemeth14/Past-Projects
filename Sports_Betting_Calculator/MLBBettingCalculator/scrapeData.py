import requests
from bs4 import BeautifulSoup
import datetime
import json
from selenium import webdriver
from selenium.webdriver.common.by import By
#from webdriver_manager.chrome import ChromeDriverManager
from urllib3.exceptions import InsecureRequestWarning
import pickle
import sklearn
import pandas as pd

requests.packages.urllib3.disable_warnings(InsecureRequestWarning)

# Fill in this path for your chrome webdriver
DanielPath = "C:\\Users\\19258\\Downloads\\chromedriverpath\\chromedriver.exe"

today = datetime.datetime.now()
currentSeason = today.strftime("%Y")
DD = datetime.timedelta(days=1)
yesterday = today - DD
DD = datetime.timedelta(days=10)
tenDaysAgo = today - DD
DD = datetime.timedelta(days=15)
fifteenDaysAgo = today - DD
DD = datetime.timedelta(days=30)
thirtyDaysAgo = today - DD


def getwRCPlus10Day(targetTeamName: str) -> int:
    wRCPlusURL = (
                'https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=8&season=' + currentSeason +
                '&month=1000&season1=' + currentSeason + '&ind=0&team=0%2Cts&rost=0&age=0&filter=&players=0&startdate=' + str(
            tenDaysAgo.date()) +
                '&enddate=' + str(yesterday.date()) + '&sort=16%2Cd')
    page = requests.get(wRCPlusURL)
    soup = BeautifulSoup(page.content, 'html.parser')

    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        wRCColumn = results.find(class_='grid_line_regular rgSorted')

        wRC = int(wRCColumn.text)

        if (teamName == targetTeamName):
            return wRC

    return 0

def getwRCPlus30Day(targetTeamName: str) -> int:
    wRCPlusURL = (
                'https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=8&season=' + currentSeason +
                '&month=1000&season1=' + currentSeason + '&ind=0&team=0%2Cts&rost=0&age=0&filter=&players=0&startdate=' + str(
            thirtyDaysAgo.date()) +
                '&enddate=' + str(yesterday.date()) + '&sort=16%2Cd')

    page = requests.get(wRCPlusURL)
    soup = BeautifulSoup(page.content, 'html.parser')

    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        wRCColumn = results.find(class_='grid_line_regular rgSorted')

        wRC = int(wRCColumn.text)

        if (teamName == targetTeamName):
            return wRC

    return 0

def getHardHitPercentage10Day(targetTeamName: str) -> float:
    hardHitURL = ('https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=24&season='
    + currentSeason + '&month=1000&season1=' + currentSeason + '&ind=0&team=0%2Cts&rost=0&age=0&filter=&players=0&startdate='
    + str(tenDaysAgo.date()) + '&enddate=' + str(yesterday.date()) + '&sort=10%2Cd')

    page = requests.get(hardHitURL)
    soup = BeautifulSoup(page.content, 'html.parser')

    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        hardHitColumn = results.find(class_='grid_line_regular rgSorted')

        hardHitRate = float(hardHitColumn.text[:-1])
        
        if (teamName == targetTeamName):
            return hardHitRate

def getBullpenSIERA(targetTeamName: str) -> float:
    SIERAurl = ('https://www.fangraphs.com/leaders.aspx?pos=all&stats=rel&lg=all&qual=0&type=1&season='
                + currentSeason + '&month=1000&season1=' + currentSeason + '&ind=0&team=0,ts&rost=0&age=0&filter=&players=0&startdate='
                + str(fifteenDaysAgo.date()) + '&enddate=' + str(yesterday.date()) + '&sort=20,d')

    page = requests.get(SIERAurl)
    soup = BeautifulSoup(page.content, 'html.parser')

    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        SIERACol = results.find(class_='grid_line_regular rgSorted')

        SIERA = float(SIERACol.text)
        if (teamName == targetTeamName):
            return SIERA

    return 0.0

def getBsR(targetTeamName: str) -> float:
    BsRURL = ('https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=8&season='
              + currentSeason + '&month=0&season1=' + currentSeason + '&ind=0&team=0,ts&rost=0&age=0&filter=&players=0&startdate='
              + currentSeason + '-01-01&enddate=' + currentSeason + '-12-31&sort=18,d')

    page = requests.get(BsRURL)
    soup = BeautifulSoup(page.content, 'html.parser')

    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        BsRCol = results.find(class_='grid_line_break rgSorted')

        BsR = float(BsRCol.text)
        if (teamName == targetTeamName):
            return BsR

    return 0.0

def getDRSperGame(targetTeamName: str) -> float:
    # Scrape stats data
    page = requests.get("https://www.fangraphs.com/leaders.aspx?pos=all&stats=fld&lg=all&qual=0&type=1&season="
                        + currentSeason + "&month=0&season1=" + currentSeason +
                        "&ind=0&team=0%2Cts&rost=0&age=0&filter=&players=0&startdate=" + currentSeason +
                        "-01-01&enddate=" + currentSeason + "-12-31&sort=11%2Cd")
    soup = BeautifulSoup(page.content, 'html.parser')

    # Go through each row until you find the row correlating to your team
    baseRowID = "LeaderBoard1_dg1_ctl00__"
    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = results.find("a").text

        # Row for your team
        if (teamName == targetTeamName):
            cols = results.find_all("td", class_="grid_line_regular")
            drs = cols[10].text
            innings = cols[2].text
            return float(drs) / (float(innings) / 81)

    return 0.0

def getGamesPlayed(targetTeamName: str) -> int:
    dateEnd = today.date()
    dateDelta = ''
    projectionMode = 2
    standingsType = 'div'

    url = 'https://www.fangraphs.com/api/playoff-odds/odds?dateEnd=' + str(dateEnd) + '&dateDelta=' + str(
        dateDelta) + '&projectionMode=' + str(projectionMode) + '&standingsType=' + str(standingsType)
    session = requests.Session()
    response = session.get(url, verify=False)
    result = json.loads(response.text)
    for team in result:
        if (team['shortName'] == targetTeamName):
            return int(team['L']) + int(team['W'])

    return 0.0

def getBSRperGame(targetTeamName: str) -> float:
    gamesPlayed = getGamesPlayed(targetTeamName)
    if(gamesPlayed > 0):
        return getBsR(targetTeamName) / getGamesPlayed(targetTeamName)
    return 0


def getStarterSIERA(startingPitcherURL: str) -> float:
    # Use url to find pitcher stats
    # Open url
    driver = webdriver.Chrome(DanielPath)
    #driver = webdriver.Chrome(ChromeDriverManager().install())
    driver.get(startingPitcherURL)

    # Go to batted-ball section
    try:
        siera = driver.find_element(By.ID, "batted-ball").find_elements_by_class_name("row-mlb-season")
    except:
        print(startingPitcherURL)
        return 0

    # Go through each potential season and check if it is the current season
    for item in siera:
        # If it's the current season get the SIERA
        try:
            seasonYear = item.find_element_by_link_text(currentSeason)
            siera = item.find_element_by_xpath("td[@data-stat='SIERA']").get_attribute("innerHTML")

            # Flag siera if it's empty
            if (siera == "" or siera == None):
                siera = 0.0

            driver.quit()
            return float(siera)

        # Otherwise skip it
        except:
            pass

    return 0


def getStarts(startingPitcherURL: str) -> int:
    # Use url to find pitcher stats
    # Open url
    driver = webdriver.Chrome(DanielPath)
    #driver = webdriver.Chrome(ChromeDriverManager().install())
    driver.get(startingPitcherURL)

    # Filtering through the html to get to the table
    try:
        starts = driver.find_element(By.ID, "root-player-pages").find_element_by_id(
            "dashboard").find_element_by_tag_name(
            "tbody").find_elements_by_class_name("row-mlb-season")
    except:
        return 1

    # Going through the rows to get to the current season
    for season in starts:
        # If it's the current season get the starts and innings pitched
        try:
            seasonYear = season.find_element_by_link_text(currentSeason)
            numStarts = int(season.find_element_by_xpath("td[@data-stat='GS']").get_attribute("innerHTML"))

            # Return 0 if player has no stats
            if (numStarts == None or numStarts == 0):
                return 1

            driver.quit()
            return numStarts

        # Otherwise skip it
        except:
            pass

    return 1


def getInnings(pitcherSplitURL: str) -> float:
    driver = webdriver.Chrome(executable_path=DanielPath)
    #driver = webdriver.Chrome(ChromeDriverManager().install())
    driver.get(pitcherSplitURL)

    try:
        innings = driver.find_element(By.ID, "root-player-pages").find_element_by_id(
            "standard").find_element_by_tag_name("tbody").find_elements_by_class_name("row-mlb-season")
    except:
        pass

    for item in innings:
        try:
            # Finding the correct season row
            dataStat = item.find_element_by_xpath("td[@data-stat='Split']").get_attribute("innerHTML")
            if (dataStat != "As Starter"):
                continue

            # Flag innings pitched if it's empty
            ip = item.find_element_by_xpath("td[@data-stat='IP']").get_attribute("innerHTML")

            # Rounding innings
            ip = float(ip)
            if (ip.is_integer() == False):
                decimal = ip - int(ip)
                ip = ((10 / 3) * decimal) + int(ip)

            driver.quit()
            return float(ip)

        # Otherwise skip it
        except:
            pass

    # Edge case
    return 0


def getINNperStart(startingPitcherURL: str, pitcherSplitURL: str) -> float:
    if (getStarts(startingPitcherURL) != 0):
        return (getInnings(pitcherSplitURL) / getStarts(startingPitcherURL))
    return 0

def getAverageHardHitRate() -> float:
    hardHitURL = ('https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=24&season=' + 
    currentSeason + '&month=0&season1=' + currentSeason + '&ind=0&team=0,ss&rost=0&age=0&filter=&players=0&startdate='
    + currentSeason + '-01-01&enddate=' + currentSeason + '-12-31&sort=10,d')
    
    page = requests.get(hardHitURL)
    soup = BeautifulSoup(page.content, 'html.parser')

    rowID = "LeaderBoard1_dg1_ctl00__0"
    results = soup.find(id=rowID)
    hardHitColumn = results.find(class_='grid_line_regular rgSorted')
    hardHitRate = float(hardHitColumn.text[:-1])
    return hardHitRate

def getOffensiveRuns(targetTeamName: str) -> float:
    numGames = getGamesPlayed(targetTeamName)

    # URL for 14 day offense
    twoWeekURL = 'https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=8&season=' + \
                 currentSeason + '&month=2&season1=' + currentSeason + '&ind=0&team=0,ts&rost=0&age=0&filter=&players=0'

    # URL for 1 month offense
    oneMonthURL = 'https://www.fangraphs.com/leaders.aspx?pos=all&stats=bat&lg=all&qual=0&type=8&season=' + \
                 currentSeason + '&month=3&season1=' + currentSeason + '&ind=0&team=0,ts&rost=0&age=0&filter=&players=0'

    # Calculate offense using a combination of those two
    twoWeekOffense = calculateRBI(targetTeamName, twoWeekURL)
    oneMonthOffense = calculateRBI(targetTeamName, oneMonthURL)

    expectedRuns = 0.6*twoWeekOffense + 0.4*oneMonthOffense
    return expectedRuns

def calculateRBI(targetTeamName: str, battingURL: str) -> float:
    page = requests.get(battingURL)
    soup = BeautifulSoup(page.content, 'html.parser')
    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)

        if (teamName == targetTeamName):
            hr = float(results.find_all(class_='grid_line_regular')[4].text)
            sb = float(results.find_all(class_='grid_line_regular')[7].text)
            bb = float(results.find_all(class_='grid_line_break')[0].text.rstrip('%')) / 100.0
            k = float(results.find_all(class_='grid_line_regular')[8].text.rstrip('%')) / 100.0
            iso = float(results.find_all(class_='grid_line_regular')[9].text)
            babip = float(results.find_all(class_='grid_line_regular')[10].text)
            avg = float(results.find_all(class_='grid_line_break')[1].text)
            obp = float(results.find_all(class_='grid_line_regular')[11].text)
            slg = float(results.find_all(class_='grid_line_regular')[12].text)
            woba = float(results.find_all(class_='grid_line_regular')[13].text)
            wrc = float(results.find_all(class_='grid_line_regular')[14].text)
            bsr = float(results.find_all(class_='grid_line_break')[3].text)

            # Convert it to pandas dataframe so the ml model can train on it
            teamData = {'HR': [hr], 'SB': [sb], "BB%": [bb], "K%": [k], "ISO": [iso], "BABIP": [babip],
                        "AVG": [avg], "OBP": [obp], "SLG": [slg], "wOBA": [woba], "wRC+": [wrc], "BsR": [bsr]}

            predict_data = pd.DataFrame(teamData,
                                        columns=["HR", "SB", "BB%", "K%", "ISO", "BABIP", "AVG", "OBP", "SLG", "wOBA",
                                                 "wRC+", "BsR"])

            # Load the model from disk
            modelFileName = "C:\\Users\\19258\\baseball-betting\\MLBBettingCalculator\\wet_unseld_secret_sauce.sav"
            #modelFileName = '/Users/benjaminroth/Betting/baseball-betting/MLBBettingCalculator/wet_unseld_secret_sauce.sav'
            model = pickle.load(open(modelFileName, 'rb'))

            # Predict the number of runs and return it
            expectedRuns = model.predict(predict_data)
            return expectedRuns[0]

    return 0

def getDefensiveRuns(startingPitcherURL: str, targetTeamName: str) -> float:
    DanielPath = "C:\\Users\\19258\\Downloads\\chromedriverpath\\chromedriver.exe"
    driver = webdriver.Chrome(DanielPath)
    driver.get(startingPitcherURL)

    # Create dictionary to store data values
    teamData = dict.fromkeys(
        ["IP_x", "K/9_x", "BB/9_x", "HR/9_x", "BABIP_x", "LOB%_x", "GB%_x", "HR/FB_x", "FIP_x", "xFIP_x", "SV_y",
         "K/9_y", "BB/9_y", "HR/9_y", "BABIP_y", "LOB%_y", "GB%_y", "HR/FB_y", "FIP_y", "xFIP_y"])

    # Filtering through the html to get to the table
    seasons = driver.find_element(By.ID, "root-player-pages").find_element_by_id(
        "dashboard").find_element_by_tag_name(
        "tbody").find_elements_by_class_name("row-mlb-season")

    # Going through the rows to get to the current season
    for season in seasons:
        # If it's the current season get the starts and innings pitched
        try:
            seasonYear = season.find_element_by_link_text(currentSeason)

            teamData['IP_x'] = float(season.find_element_by_xpath("td[@data-stat='IP']").get_attribute("innerHTML"))

            # Divide innings pitched by number of starts to get the innings pitched per start
            numStarts = getStarts(startingPitcherURL)

            if(numStarts > 0):
                teamData['IP_x'] /= numStarts

            teamData['K/9_x'] = float(season.find_element_by_xpath("td[@data-stat='K/9']").get_attribute("innerHTML"))
            teamData['BB/9_x'] = float(season.find_element_by_xpath("td[@data-stat='BB/9']").get_attribute("innerHTML"))
            teamData['HR/9_x'] = float(season.find_element_by_xpath("td[@data-stat='HR/9']").get_attribute("innerHTML"))
            teamData['BABIP_x'] = float(
                season.find_element_by_xpath("td[@data-stat='BABIP']").get_attribute("innerHTML"))
            teamData['LOB%_x'] = float(
                season.find_element_by_xpath("td[@data-stat='LOB%']").get_attribute("innerHTML").rstrip('%')) / 100.0
            teamData['GB%_x'] = float(
                season.find_element_by_xpath("td[@data-stat='GB%']").get_attribute("innerHTML").rstrip('%')) / 100.0
            teamData['HR/FB_x'] = float(
                season.find_element_by_xpath("td[@data-stat='HR/FB']").get_attribute("innerHTML").rstrip('%')) / 100.0
            teamData['FIP_x'] = float(season.find_element_by_xpath("td[@data-stat='FIP']").get_attribute("innerHTML"))
            teamData['xFIP_x'] = float(season.find_element_by_xpath("td[@data-stat='xFIP']").get_attribute("innerHTML"))
            driver.quit()
        # Otherwise skip it
        except:
            pass

    # Getting bullpen data
    page = requests.get("https://www.fangraphs.com/leaders.aspx?pos=all&stats=rel&lg=all&qual=0&type=8&season=" + currentSeason +
                        "&month=0&season1=" + currentSeason + "&ind=0&team=0,ts&rost=0&age=0&filter=&players=0&startdate=&enddate=")
    soup = BeautifulSoup(page.content, 'html.parser')
    baseRowID = "LeaderBoard1_dg1_ctl00__"

    for i in range(30):
        rowID = baseRowID + str(i)
        results = soup.find(id=rowID)
        teamName = str(results.find_all(class_='grid_line_regular')[1].text)
        if (teamName == targetTeamName):
            teamData["SV_y"] = float(results.find_all(class_='grid_line_regular')[4].text)
            teamData['K/9_y'] = float(results.find_all(class_='grid_line_break')[0].text)
            teamData['BB/9_y'] = float(results.find_all(class_='grid_line_regular')[8].text)
            teamData['HR/9_y'] = float(results.find_all(class_='grid_line_regular')[9].text)
            teamData['BABIP_y'] = float(results.find_all(class_='grid_line_regular')[10].text)
            teamData['LOB%_y'] = float(results.find_all(class_='grid_line_regular')[11].text.rstrip('%')) / 100.0
            teamData['GB%_y'] = float(results.find_all(class_='grid_line_regular')[12].text.rstrip('%')) / 100.0
            teamData['HR/FB_y'] = float(results.find_all(class_='grid_line_regular')[13].text.rstrip('%')) / 100.0
            teamData['FIP_y'] = float(results.find_all(class_='grid_line_regular')[14].text)
            teamData['xFIP_y'] = float(results.find_all(class_='grid_line_regular')[15].text)

    # Load the dictionary into a pandas dataframe so the ML algorithm can train on it
    predict_data = pd.DataFrame(teamData, index=[0])

    # Load the model from disk
    modelFileName = "C:\\Users\\19258\\baseball-betting\\MLBBettingCalculator\\wet_unseld_special_sauce.sav"
    model = pickle.load(open(modelFileName, 'rb'))
    era = model.predict(predict_data)
    return(era[0])