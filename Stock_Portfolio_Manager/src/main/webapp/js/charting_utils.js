var $progress = $('.progress');
var $progressBar = $('.progress-bar');

function animate() {
    var progress = 0;      // initial value of your progress bar
    var timeout = 10;      // number of milliseconds between each frame
    var increment = 0.5;    // increment for each frame
    var maxprogress = 110; // when to leave stop running the animation
    $progress.css('display', 'flex');
    const recurse = () => {
        setTimeout(function () {
            progress += increment;
            if (progress < maxprogress) {
                $progressBar.css("width", `${progress}%`);
                recurse();
            } else {
                $progressBar.css("width", `0%`);
                progress = 0;
            }
        }, timeout);
    }
    recurse();
};

const COLORS = ["#0000FF", "#008000", "#00FFFF", "#FFFF00", "#FF0000", "#808080", "#FF00FF", "#000000"];
let cIndex = 0;
function generateRandomColor() {
    const color = COLORS[cIndex];
    cIndex = (cIndex+1) % COLORS.length;
    return color;
}

function decorateWithChartSettings(ds) {
    return {
        ...ds,
        fill: false,
        borderColor: generateRandomColor(),
        lineTension: 0,
        pointRadius: 2,
    };
}

function getUnixDate(date) {
    return Math.floor((+ date) / 1000);
}

function getDefaultLabels() {
    const now = moment();
    const temp = moment(now);
    const labels = [];
    while (now.diff(temp, 'days') < 90) {
        labels.push(temp.format('MM/DD'));
        temp.subtract(1, 'days');

        let day = temp.day();
        while ((day === 6) || (day === 0)) {
            temp.subtract(1, 'days');
            day = temp.day();
        }
    }
    labels.reverse();
    return labels;
}

function createChart(context, data) {
    cIndex = 0;
    return new Chart(ctx, {
        type: 'line',
        data: _.cloneDeep(data),
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: '% change'
                    }
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Date'
                    },
                }]
            }
        }
    })
}