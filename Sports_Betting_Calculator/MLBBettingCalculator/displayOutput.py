def display(roadTeam: str, homeTeam: str, winProbabilities: (float, float), roadPitcherGames: int, homePitcherGames: int):
    if (winProbabilities[0] == 0 or winProbabilities[1] == 0):
        print(f'{roadTeam} vs. {homeTeam} - check manually')
        print()
        return

    # Case where pitcher has played small sample size
    if(roadPitcherGames < 5): print("WARNING! Road pitcher has started less than 5 games, check manually")
    if(homePitcherGames < 5): print("WARNING! Home pitcher has started less than 5 games, check manually")

    roadTeamPercentage = round(winProbabilities[0]*100, 3)
    homeTeamPercentage = round(winProbabilities[1]*100, 3)
    
    if (winProbabilities[0]*.95 > 0.5):
        roadTeamML = str(round(-.95*(winProbabilities[0]/(1-winProbabilities[0])*100)))
    else:
        roadTeamML = str('+' + str(round(1.05*((1-winProbabilities[0])/winProbabilities[0]*100))))
    
    if (winProbabilities[1]*.95 > 0.5):
        homeTeamML = str(round(-.95*(winProbabilities[1]/(1-winProbabilities[1])*100)))
    else:
        homeTeamML = str('+' + str(round(1.05*((1-winProbabilities[1])/winProbabilities[1]*100))))
    
    print(f'{roadTeam} - {roadTeamPercentage}%: {roadTeamML} ', end = "")
    if (roadTeamML[0] == '-'):
        print('or lower')
    else:
        print('or higher')

    print(f'{homeTeam} - {homeTeamPercentage}%: {homeTeamML} ', end = "")
    if (homeTeamML[0] == '-'):
        print('or lower')
    else:
        print('or higher')

    print()    
    
    