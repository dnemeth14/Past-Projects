import scrapeData

class Game:
    def __init__(self, roadTeam: str, roadPitcher: str, roadPitcherURL: str, roadSplitURL: str, homeTeam: str, homePitcher: str, homePitcherURL: str, homeSplitURL: str):
        self.roadTeam = roadTeam
        self.roadPitcher = roadPitcher
        self.roadPitcherURL = roadPitcherURL
        self.roadSplitURL = roadSplitURL
        self.roadTeamData = {}
        self.homeTeam = homeTeam
        self.homePitcher = homePitcher
        self.homePitcherURL = homePitcherURL
        self.homeSplitURL = homeSplitURL
        self.homeTeamData = {}

    def getOGData(self):
        #scrape the data and put it into roadTeamData attribute
        self.roadTeamData['starterSIERA'] = float(scrapeData.getStarterSIERA(self.roadPitcherURL))
        self.roadTeamData['INN/START'] = round(scrapeData.getINNperStart(self.roadPitcherURL, self.roadSplitURL), 3)
        self.roadTeamData['bullpenSIERA'] = scrapeData.getBullpenSIERA(self.roadTeam)
        self.roadTeamData['wRCPlus10Day'] = scrapeData.getwRCPlus10Day(self.roadTeam)
        self.roadTeamData['wRCPlus30Day'] = scrapeData.getwRCPlus30Day(self.roadTeam)
        self.roadTeamData['hardHitPercentage10Day'] = scrapeData.getHardHitPercentage10Day(self.roadTeam)
        self.roadTeamData['BsR/Game'] = round(scrapeData.getBSRperGame(self.roadTeam), 5) #Round to 5th decimal place for floats
        self.roadTeamData['DRS/Game'] = round(scrapeData.getDRSperGame(self.roadTeam), 5)
        
        self.homeTeamData['starterSIERA'] = float(scrapeData.getStarterSIERA(self.homePitcherURL))
        self.homeTeamData['INN/START'] = round(scrapeData.getINNperStart(self.homePitcherURL, self.homeSplitURL), 3)
        self.homeTeamData['bullpenSIERA'] = scrapeData.getBullpenSIERA(self.homeTeam)
        self.homeTeamData['wRCPlus10Day'] = scrapeData.getwRCPlus10Day(self.homeTeam)
        self.homeTeamData['wRCPlus30Day'] = scrapeData.getwRCPlus30Day(self.homeTeam)
        self.homeTeamData['hardHitPercentage10Day'] = scrapeData.getHardHitPercentage10Day(self.homeTeam)
        self.homeTeamData['BsR/Game'] = round(scrapeData.getBSRperGame(self.homeTeam), 5) #Round to 5th decimal place for floats
        self.homeTeamData['DRS/Game'] = round(scrapeData.getDRSperGame(self.homeTeam), 5)

        self.roadTeamData['pitcherGamesPlayed'] = scrapeData.getStarts(self.roadPitcherURL)
        self.homeTeamData['pitcherGamesPlayed'] = scrapeData.getStarts(self.homePitcherURL)

    def getMLData(self):
        #Machine learning data
        self.roadTeamData['pitcherGamesPlayed'] = scrapeData.getStarts(self.roadPitcherURL)
        self.homeTeamData['pitcherGamesPlayed'] = scrapeData.getStarts(self.homePitcherURL)
        self.roadTeamData['DRS/Game'] = round(scrapeData.getDRSperGame(self.roadTeam), 5)
        self.homeTeamData['DRS/Game'] = round(scrapeData.getDRSperGame(self.homeTeam), 5)
        self.roadTeamData['RunsBatted'] = scrapeData.getOffensiveRuns(self.roadTeam)
        self.roadTeamData['RunsAllowed'] = scrapeData.getDefensiveRuns(self.roadPitcherURL, self.roadTeam)
        self.homeTeamData['RunsBatted'] = scrapeData.getOffensiveRuns(self.homeTeam)
        self.homeTeamData['RunsAllowed'] = scrapeData.getDefensiveRuns(self.homePitcherURL, self.homeTeam)


