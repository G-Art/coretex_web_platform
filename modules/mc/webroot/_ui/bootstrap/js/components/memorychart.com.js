// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';
Vue.component('memory-chart', {
    props: ['title'],
    template: `<div class="card mb-3" >
            <div class="card-header">
                <i class="fas fa-chart-area"></i>
                {{title}}
            </div>
            <div class="card-body">
                <canvas id="memChart" width="100%" height="30"></canvas>
            </div>
        </div>`



});



