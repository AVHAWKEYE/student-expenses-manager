// YEARLY EXPENSES BAR CHART
const yearCtx = document.getElementById('yearChart');
if (yearCtx) {
    const yearChart = new Chart(yearCtx, {
        type: 'bar',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Expenses in ₹',
                data: [1200, 1500, 1100, 1800, 1700, 1600, 1400, 1300, 1500, 2000, 2200, 2100],
                backgroundColor: 'rgba(54, 162, 235, 0.6)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true }
            },
            responsive: true
        }
    });
}

// MONTHLY EXPENSES BAR CHART
const monthCtx = document.getElementById('monthChart');
if (monthCtx) {
    const monthChart = new Chart(monthCtx, {
        type: 'bar',
        data: {
            labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
            datasets: [{
                label: 'Expenses in ₹',
                data: [400, 500, 300, 600],
                backgroundColor: 'rgba(255, 99, 132, 0.6)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true }
            },
            responsive: true
        }
    });
}

// WEEKLY EXPENSES BAR CHART
const weekCtx = document.getElementById('weekChart');
if (weekCtx) {
    const weekChart = new Chart(weekCtx, {
        type: 'bar',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            datasets: [{
                label: 'Expenses in ₹',
                data: [50, 100, 80, 70, 90, 60, 40],
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: { beginAtZero: true }
            },
            responsive: true
        }
    });
}
