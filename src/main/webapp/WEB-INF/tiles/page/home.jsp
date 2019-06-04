<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/jquery@3.4.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="//cdn.jsdelivr.net/npm/chart.js@2.8.0/dist/Chart.min.js"></script>

<ul>
    <li><spring:message code="home.currenttemperature" /> : <span id="temperature">${current["temperature"]} *C (${current["temperatureDate"]})</span></li>
    <li><spring:message code="home.currenthumidity" /> : <span id="humidity">${current["humidity"]} % (${current["humidityDate"]})</span></li>
</ul>

<canvas id="myChart" width="400" height="100"></canvas>
<script type="text/javascript">
$(document).ready(function() {
    
    
    $.ajax({
        url: '<c:url value="/data/home.RPi.DHT11/temperature/recent" />',
        success: function(data, textStatus, xhr) {
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                newData.xAxis.push(new Date(data.datetime).toLocaleString());
                newData.yAxis.push(Number(data.value));
            });
            
            chart = createChart(newData);
            chart.data.datasets.push({
                label: 'temperature',
                yAxisID: 'Temperature',
                borderColor: '#ffbc08',
                data: newData.yAxis
            });
            chart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
    
    setTimeout(function() {
        
    
    $.ajax({
        url: '<c:url value="/data/home.RPi.DHT11/humidity/recent" />',
        success: function(data, textStatus, xhr) {
            
            var newData = {'xAxis': [], 'yAxis': []};
            data.forEach(function(data, index) {
                newData.xAxis.push(new Date(data.datetime));
                newData.yAxis.push(Number(data.value));
                //newData.yAxis[1].push(Number(data.value)-3);
            });
            
            
            window.myChart.data.datasets.push({
                label: 'Humidity',
                yAxisID: 'Humidity',
                borderColor: '#1878f0',
                data: newData.yAxis
            });
            window.myChart.update();
        },
        error: function(xhr, textStatus, errorThrown) {
            console.log('error');
            console.log(xhr);
            console.log(textStatus);
            console.log(errorThrown);
            console.log(data);
        }
    });
    
    }, 1000);
});

function createChart(data) {
    var ctx = document.getElementById('myChart').getContext('2d');
    window.myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.xAxis,
        },
        options: {
            scales: {
                yAxes: [{
                    id: 'Temperature',
                    type: 'linear',
                    position: 'left',
                    ticks: {
                        fontColor: '#ffbc08',
                        min: 20,
                        max: 40
                    }
                }, {
                    id: 'Humidity',
                    type: 'linear',
                    position: 'right',
                    ticks: {
                        fontColor: '#1878f0',
                        max: 100,
                        min: 0
                    }
                }]
            }
        }
    });
    return window.myChart;
}

</script>