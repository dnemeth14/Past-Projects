def calculateWinProbabilities(roadTeamData: dict, homeTeamData: dict, averageHardHitRate: float) -> (float, float):
    if (homeTeamData['starterSIERA'] == 0 or roadTeamData['starterSIERA'] == 0
    or homeTeamData['INN/START'] == 0 or roadTeamData['INN/START'] == 0):
        return (0,0)
    
    homeTeamAverageWRC = 0.4 * homeTeamData['wRCPlus10Day'] + 0.3 * homeTeamData['wRCPlus30Day'] + 0.3 * (100 * (homeTeamData['hardHitPercentage10Day']/averageHardHitRate))
    roadTeamAverageWRC = 0.4 * roadTeamData['wRCPlus10Day'] + 0.3 * roadTeamData['wRCPlus30Day'] + 0.3 * (100 * (roadTeamData['hardHitPercentage10Day']/averageHardHitRate))
    
    homeTeamAverageSIERA = (homeTeamData['starterSIERA'] * (homeTeamData['INN/START']/9)) + (homeTeamData['bullpenSIERA'] * ((9-homeTeamData['INN/START'])/9))
    roadTeamAverageSIERA = (roadTeamData['starterSIERA'] * (roadTeamData['INN/START']/9)) + (roadTeamData['bullpenSIERA'] * ((9-roadTeamData['INN/START'])/9))

    homeTeamExpectedRuns = (0.01 * homeTeamAverageWRC * roadTeamAverageSIERA) - 0.6 * roadTeamData['DRS/Game'] + 0.5 * homeTeamData['BsR/Game']
    roadTeamExpectedRuns = (0.01 * roadTeamAverageWRC * homeTeamAverageSIERA) - 0.6 * homeTeamData['DRS/Game'] + 0.5 * roadTeamData['BsR/Game']
    
    homeTeamExpectedWinPercentage = (homeTeamExpectedRuns ** 1.81) / ((roadTeamExpectedRuns ** 1.81)+(homeTeamExpectedRuns ** 1.81)) * 1.03
    return (1-homeTeamExpectedWinPercentage, homeTeamExpectedWinPercentage)
   

def predictWinProbabilities(roadTeamData: dict, homeTeamData: dict) -> (float, float):
    if(homeTeamData['RunsBatted'] == 0 or roadTeamData['RunsBatted'] == 0): return (0,0)
    if (homeTeamData['RunsAllowed'] == 0 or roadTeamData['RunsAllowed'] == 0): return (0, 0)

    homeTeamExpectedRuns = (roadTeamData['RunsAllowed']*2 + homeTeamData['RunsBatted'])/3 - roadTeamData['DRS/Game']
    roadTeamExpectedRuns = (homeTeamData['RunsAllowed']*2 + roadTeamData['RunsBatted'])/3 - homeTeamData['DRS/Game']

    homeTeamExpectedWinPercentage = (homeTeamExpectedRuns ** 1.81) / ((roadTeamExpectedRuns ** 1.81)+(homeTeamExpectedRuns ** 1.81)) * 1.03
    return (1-homeTeamExpectedWinPercentage, homeTeamExpectedWinPercentage)
