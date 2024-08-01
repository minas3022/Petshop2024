 function limparFormulario() {
            document.getElementById('meuFormulario').reset();
            // document.getElementById('meuBotao').value = '';
        }

        document.getElementById('cancelarBtn').addEventListener('click', function(event) {
            event.preventDefault();
            limparFormulario();
        });