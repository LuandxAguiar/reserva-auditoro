function fazPost(url, body){

    console.log("Body=", body)

    let request = new XMLHttpRequest()

    request.open("POST", url, true)

    request.setRequestHeader("Content-type", "application/json")

    request.send(JSON.stringify(body))



    request.onload = function(){

        console.log(this.responseText)

    }

    return request.responseText

}
function cadastrarUsuario(){

    event.preventDefault()

    let url = "https://10.92.198.27/api/reserva"

    let nome = document.getElementById("nome")

    let nif = document.getElementById("nif")

    let email = document.getElementById("email")

    let senha = document.getElementById("senha")

    let usuario = document.getElementById("input1")

    let administrador = document.getElementById("input2")

    console.log(nome.value)

    console.log(nif.value)

    console.log(email.value)

    console.log(senha.value)

    console.log(input1.value)

    console.log(input2.value)



    body = {

        "name": nome,

        "emial":email

    }



    fazPost(url, body)



}

