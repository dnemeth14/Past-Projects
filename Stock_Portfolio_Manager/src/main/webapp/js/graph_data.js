/*
 * This file contains js functions to fetch data and update our graph
 */

let startIdx = 0;

function setOneWeek({ lineChart, globalData }) {
    const gdClone = _.cloneDeep(globalData);

    startIdx = gdClone.labels.length -5;
    const filteredData = gdClone.datasets.map((dataset) => dataset.data.slice(-5));
    const filteredLabels = gdClone.labels.slice(-5);

    lineChart.data.labels = [...filteredLabels];
    lineChart.data.datasets.forEach((ds, idx) => {
        Object.assign(ds, { data: filteredData[idx] });
    });

    lineChart.update();
}

function setOneMonth({ lineChart, globalData }) {
    const gdClone = _.cloneDeep(globalData);

    const slice = -Math.ceil(gdClone.labels.length / 12);
    startIdx = gdClone.labels.length + slice;
    const filteredData = gdClone.datasets.map((dataset) => dataset.data.slice(slice));
    const filteredLabels = gdClone.labels.slice(slice);

    lineChart.data.labels = [...filteredLabels];
    lineChart.data.datasets.forEach((ds, idx) => {
        Object.assign(ds, { data: filteredData[idx] });
    });

    lineChart.update();
}

function setThreeMonth({ lineChart, globalData }) {
    const gdClone = _.cloneDeep(globalData);

    const slice = -Math.ceil(gdClone.labels.length / 4);
    startIdx = gdClone.labels.length + slice;
    const filteredData = gdClone.datasets.map((dataset) => dataset.data.slice(slice));
    const filteredLabels = gdClone.labels.slice(slice);

    lineChart.data.labels = [...filteredLabels];
    lineChart.data.datasets.forEach((ds, idx) => {
        Object.assign(ds, { data: filteredData[idx] });
    });

    lineChart.update();
}

function setSixMonth({ lineChart, globalData }) {
    const gdClone = _.cloneDeep(globalData);

    const slice = -Math.ceil(gdClone.labels.length / 2);
    startIdx = gdClone.labels.length + slice;
    const filteredData = gdClone.datasets.map((dataset) => dataset.data.slice(slice));
    const filteredLabels = gdClone.labels.slice(slice);

    lineChart.data.labels = [...filteredLabels];
    lineChart.data.datasets.forEach((ds, idx) => {
        Object.assign(ds, { data: filteredData[idx] });
    });

    lineChart.update();
}

function setOneYearData({ lineChart, globalData }) {
    startIdx = 0;
    lineChart.data = _.cloneDeep(globalData);
    lineChart.update();
}

function zoom({ lineChart, globalData, out }) {
    const gdClone = _.cloneDeep(globalData);

    startIdx = startIdx + (out ? -15 : 15);
    if (startIdx < 0) {
        startIdx = 0;
    } else if (startIdx >= gdClone.labels.length) {
        startIdx = gdClone.labels.length - 4;
    }

    const filteredData = gdClone.datasets.map((dataset) => dataset.data.slice(startIdx));
    const filteredLabels = gdClone.labels.slice(startIdx);

    lineChart.data.labels = [...filteredLabels];
    lineChart.data.datasets.forEach((ds, idx) => {
        Object.assign(ds, { data: filteredData[idx] });
    });

    lineChart.update();
}