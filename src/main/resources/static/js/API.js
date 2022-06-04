 const ul = document.getElementById('reserva')
    const url = 'http://localhost:8080/index'
    
    fetch(url)
        .then((resp) => resp.json())
        .then(data => {
            let restaurantes = data
            return restaurantes.map((reserva) => {
               
                
                let span = createNode('span')
                span.innerHTML = `${reserva.id} ${reserva.nome}  ${reserva.data}`
                
                
                })
                
                
              
            })
   
        .catch((error) => {
            console.log(error);
        })

    function createNode(element) {
        return document.createElement(element)
    }

    function append(parent, el) {
        return parent.appendChild(el);
    }