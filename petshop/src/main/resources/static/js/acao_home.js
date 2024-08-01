document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById('satisfactionForm');
    const ctx = document.getElementById('satisfactionChart').getContext('2d');
    let satisfactionChart;

    if (form) {
        form.addEventListener('submit', handleFormSubmit);
    }

    function handleFormSubmit(event) {
        event.preventDefault();

        const rating = document.querySelector('input[name="rating"]:checked');
        const email = document.getElementById('email').value;

        if (rating && email) {
            saveFeedback(rating.value, email);
            alert('Obrigado pela sua avaliação!');
            updateChart();
            form.reset();
        } else {
            alert('Por favor, selecione uma avaliação e insira seu e-mail.');
        }
    }

    function saveFeedback(rating, email) {
        const feedbacks = JSON.parse(localStorage.getItem('feedbacks')) || [];
        feedbacks.push({ rating, email });
        localStorage.setItem('feedbacks', JSON.stringify(feedbacks));
    }

    function updateChart() {
        const feedbacks = JSON.parse(localStorage.getItem('feedbacks')) || [];


        const ratings = [0, 0, 0, 0, 0];
        feedbacks.forEach(feedback => {
            const ratingIndex = feedback.rating - 1;
            if (ratingIndex >= 0 && ratingIndex < 5) {
                ratings[ratingIndex]++;
            }
        });

        const data = {
            labels: ['1 Estrela', '2 Estrelas', '3 Estrelas', '4 Estrelas', '5 Estrelas'],
            datasets: [{
                label: 'Quantidade de Avaliações',
                data: ratings,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        };

        if (satisfactionChart) {
            satisfactionChart.destroy();
        }

        satisfactionChart = new Chart(ctx, {
            type: 'bar',
            data: data,
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    function clearStorage() {
        localStorage.clear();
    }

    clearStorage();

    updateChart();
});


