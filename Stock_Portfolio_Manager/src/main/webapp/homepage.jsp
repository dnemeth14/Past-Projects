<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // redirect if not authenticated
    String USERNAME = (String) session.getAttribute("username");
    if (USERNAME == null) {
        response.sendRedirect("/");
        return;
    }
	
	String portfolioValue = "";
	if (request.getAttribute("portfolioValue") != null) {
	    portfolioValue = (String) request.getAttribute("portfolioValue");
	    System.out.println("Portfolio Value: " + portfolioValue);
	}
	
	String percentChange = "";
	if (request.getAttribute("percentChange") != null) {
	    percentChange = (String) request.getAttribute("percentChange");
	    System.out.println("Percent Change: " + percentChange);
	}
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="static/css/custom.css">
    <link rel="stylesheet" href="../css/homepage.css">
    <link rel="stylesheet" href="static/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<nav class="navbar navbar-dark pageBanner">
    <span class="navbar-brand mb-0 h1">USC CS 310 Stock Portfolio Management</span>
    <div class="logout">
        <form name="logout-form" action="LogoutServlet">
            <div id="logout-btn">
                <input type="button" value="Logout" onclick="submit()" />
            </div>
        </form>
    </div>
</nav>

<div class="container">
	<h2>Portfolio Performance</h2>
	<div class="row">
		<div class="col-sm">
			<p id="dollarAmount"><%= portfolioValue %></p>
		</div>
		<div class="col-sm">
			<p id="percentChange"><%= percentChange %></p>
		</div>
		<div class="col-sm">
		</div>
	</div>
</div>

<div class="container">
    <h2>Stock Performance</h2>
    <div class="chart-container">
        <div class="progress">
            <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div>
        </div>
        <canvas id="canvas"></canvas>
    </div>
    <div class="container" id="graph-controls">
        <div class="row justify-content-between">
            <div class="col-12 col-sm-5">
                <div class="row my-2">
                    <div class="col-6 col-sm-4">
                        <label>From: </label>
                        <div id="datepicker-from" class="input-group date" data-date-format="mm-dd-yyyy">
                            <input class="form-control" type="text" readonly />
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                        </div>
                    </div>
                    <div class="col-6 col-sm-4">
                        <label>To: </label>
                        <div id="datepicker-to" class="input-group date" data-date-format="mm-dd-yyyy">
                            <input class="form-control" type="text" readonly />
                            <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                        </div>
                    </div>
                    <div class="col-12 col-sm-4">
                        <label></label>
                        <div>
                            <button type="button" id="date-range-btn" class="btn btn-secondary mt-4 w-100">Submit</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-12 col-sm-5">
                <div class="container">
                    <div class="row justify-content-end">
                        <div>
                            <div class="btn-group" role="group" aria-label="Basic example">
                                <button type="button" class="btn btn-secondary" id="one-d-btn">1D</button>
                                <button type="button" class="btn btn-secondary" id="one-w-btn">1W</button>
                                <button type="button" class="btn btn-secondary" id="one-m-btn">1M</button>
                                <button type="button" class="btn btn-secondary" id="three-m-btn">3M</button>
                                <button type="button" class="btn btn-secondary" id="six-m-btn">6M</button>
                                <button type="button" class="btn btn-secondary" id="one-y-btn">1Y</button>
                            </div>
                            <div class="btn-group my-2" role="group" aria-label="Basic example 2">
                                <button type="button" class="btn btn-secondary" id="zoom-out-btn">-</button>
                                <button type="button" class="btn btn-secondary" id="zoom-in-btn">+</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="static/js/jquery-3.5.1.min.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/Chart.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lodash@4.17.20/lodash.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js" integrity="sha512-qTXRIMyZIFb8iQcfjXWCO8+M5Tbc38Qi5WzdPOYZHIlZpzBHG3L3by84BBBOiRGiEb7KKtAOAs5qYdUiZiQNNQ==" crossorigin="anonymous"></script>
<script src="js/charting_utils.js"></script>
<script src="js/graph_data.js"></script>
<script>

	//Portfolio summary code
	let portfolioSummary = document.getElementById("dollarAmount");

	//Graphing code
    let ctx = document.getElementById("canvas").getContext("2d");

    let myLineChart;

    const USERNAME = "<%=session.getAttribute("username")%>";
    const today = new Date();
<<<<<<< HEAD
    let threeMonthsAgo = new Date();
    threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);

=======
    let twelveMonthsAgo = new Date();
    twelveMonthsAgo.setFullYear(twelveMonthsAgo.getFullYear()-1);
	
    let monthAgo = new Date();
    monthAgo.setMonth(monthAgo.getMonth() - 1);
    
>>>>>>> feature-homepage-portfolio
    let globalData = undefined;
    let globalFrom;
    let globalTo;

    let fetchData = ({ from, to }, callback) => {
        globalFrom = from;
        globalTo = to;
        const formattedFrom = getUnixDate(from);
        const formattedTo = getUnixDate(to);
        animate();
        cIndex = 0;

        $("#graph-controls").css("display", "none");
        $.get(`/graphdata?username=<%=USERNAME%>&from=${formattedFrom}&to=${formattedTo}`, function (data, text) {
            console.log(data);
            if (data.labels.length == 0) {
                Object.assign(data, { labels: getDefaultLabels() });
            } else {
                const decoratedDataSets = data.datasets.map(decorateWithChartSettings);
                Object.assign(data, { datasets: decoratedDataSets });
            }

            globalData = data;
            callback(data);
            $progress.css('display', 'none');
            $("#graph-controls").css("display", "block");
        });
    }

<<<<<<< HEAD
    $(document).ready(function() {
        fetchData({ from: threeMonthsAgo, to: today }, (data) => {
            myLineChart = createChart(ctx, data);
        });
=======
    let fetchData = (url) => $.get(url, function(data, text) {
        globalData = data;
        const decoratedDataSets = data.datasets.map(decorateWithChartSettings);
        Object.assign(globalData, { datasets: decoratedDataSets });
        $(".progress").css("display", "none");
        myLineChart = createChart(ctx, data);
        setThreeMonth({lineChart: myLineChart, globalData});
    });
    
    let dollarChange = undefined;
    let percentChange = undefined;
    let getSummary = (url) => $.get(url, function(data, text) {
    	dollarChange 
    });

    animate();
    $(document).ready(function() {
    	//Call portfolio servlet
    	const portfolioUrl = `/portfoliosummary?userId=<%=HARDCODED_USER_ID%>&from=${getUnixDate(monthAgo)}&to=${getUnixDate(today)}`;
    	getSummary(portfolioUrl);
    	
    	//Call graphing servlet
        const onLoadUrl = `/graphdata?userId=<%=HARDCODED_USER_ID%>&from=${getUnixDate(twelveMonthsAgo)}&to=${getUnixDate(today)}`;
        fetchData(onLoadUrl);
>>>>>>> feature-homepage-portfolio
    });

    $("#one-w-btn").click(function() {
        const today = moment();
        const oneWeekAgo = moment();
        oneWeekAgo.subtract(7, 'days');
        fetchData({ from: oneWeekAgo.toDate(), to: today.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        });
    });

    $("#one-m-btn").click(function() {
        const today = moment();
        const oneMonthAgo = moment();
        oneMonthAgo.subtract(1, 'months');
        fetchData({ from: oneMonthAgo.toDate(), to: today.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        });
    });

    $("#three-m-btn").click(function() {
        const today = moment();
        const threeMonthsAgo = moment();
        threeMonthsAgo.subtract(3, 'months');
        fetchData({ from: threeMonthsAgo.toDate(), to: today.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        });
    });

    $("#six-m-btn").click(function() {
        const today = moment();
        const sixMonthsAgo = moment();
        sixMonthsAgo.subtract(6, 'months');
        fetchData({ from: sixMonthsAgo.toDate(), to: today.toDate }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        });
    });

    $("#one-y-btn").click(function() {
        const today = moment();
        const oneYearAgo = moment();
        oneYearAgo.subtract(1, 'year');
        fetchData({ from: oneYearAgo.toDate(), to: today.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        })
    });

    $("#zoom-in-btn").click(function() {
        let mFrom = moment(globalFrom);
        const mTo = moment(globalTo);

        if (mTo.diff(mFrom, 'days') === 3) {
            return;
        }

        mFrom.add(15, 'days');

        if (mTo.diff(mFrom, 'days') < 3) {
            mFrom = moment(mTo);
            mFrom.subtract(3, 'days');
        }

        fetchData({ from: mFrom.toDate(), to: mTo.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        });
    });

    $("#zoom-out-btn").click(function() {
        let mFrom = moment(globalFrom);
        const mTo = moment(globalTo);

        if (mTo.diff(mFrom, 'days') === 365) {
            return;
        }

        mFrom.subtract(15, 'days');
        if (mTo.diff(mFrom, 'days') > 365) {
            mFrom = moment(mTo);
            mFrom.subtract(365, 'days');
        }

        fetchData({ from: mFrom.toDate(), to: mTo.toDate() }, (data) => {
            myLineChart.data = data
            myLineChart.update();
        })
    });

    $(function () {
        $("#datepicker-from").datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: '-3d',
        });
    });

    $(function () {
        $("#datepicker-to").datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: 'today',
        });
    });
</script>
</html>