new Vue({
    el: '#wrapper',
    data: {},
    methods: {


        createChart: function (chartData) {

            let totalMemoryDataset = {
                label: "Using KB",
                lineTension: 0.3,
                backgroundColor: "rgba(2,117,216,0.2)",
                borderColor: "rgba(2,117,216,1)",
                pointRadius: 3,
                pointBackgroundColor: "rgba(2,117,216,1)",
                pointBorderColor: "rgba(255,255,255,0.8)",
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(2,117,216,1)",
                pointHitRadius: 50,
                pointBorderWidth: 1
            };
            let jvmMemDataset = {
                label: "Allocated for jvm KB",
                lineTension: 0.3,
                backgroundColor: "rgba(255, 5, 5, 0.2)",
                borderColor: "rgba(255, 5, 5, 0.8)",
                pointRadius: 3,
                pointBackgroundColor: "rgba(255, 5, 5,1)",
                pointBorderColor: "rgba(255,255,255,0.8)",
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(255, 5, 5,1)",
                pointHitRadius: 50,
                pointBorderWidth: 1
            };
            chartData.data.datasets = [totalMemoryDataset, jvmMemDataset];

            const ctx = document.getElementById('memChart');
            const myChart = new Chart(ctx, {
                type: chartData.type,
                data: chartData.data,
                options: chartData.options,
            });


            setInterval(function () {
                const url = "memUseInfo";
                let vm = this;
                let info;
                let config = myChart.config;


                fetch(url, {
                    method: "get",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
                    }
                }).then(
                    response => response.text()
                ).then(
                    result => {
                        info = JSON.parse(result);

                        config.data.labels.push(new Date());
                        totalMemoryDataset.data.push((info.totalMemory - info.freeMemory)/1024);
                        jvmMemDataset.data.push(info.totalMemory/1024);
                        config.options.scales.yAxes.forEach(function (yAxe) {
                            yAxe.ticks.max = info.maxMemory/1024;
                        });
                        myChart.update();
                    }
                );

                if (config.data.labels.length > 100) {
                    config.data.labels.splice(0, 1); // remove the label first
                    totalMemoryDataset.data.splice(0, 1);
                    jvmMemDataset.data.splice(0, 1);
                    myChart.update();
                }


            }, 5000);
        }


    },

    mounted: function () {
        const chartData = {
            type: 'line',
            data: {
                datasets: [],
            },
            options: {
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'date'
                        },
                        gridLines: {
                            display: false
                        },
                        ticks: {
                            maxTicksLimit: 7,
                            display: false
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 100000,
                            maxTicksLimit: 10
                        },
                        gridLines: {
                            color: "rgba(0, 0, 0, .125)",
                        }
                    }],
                },
                legend: {
                    display: false
                }
            }
        };
        this.createChart(chartData);
    },
});
