indexMain();

function indexMain() {
    const DEFAULT_OPTION = "Escolha o período";
    let calendar;
	
    getElements();
    addListeners();
    initCalendar();

    function getElements() {
        selecElem = document.getElementById("categoryFilter");
    }

    function addListeners() {
        selecElem.addEventListener("change", filterEntries, false);
    }


    //select 
    function filterEntries() {
        let seletion = selecElem.value;

        //Empyt the table, keeping the first row
        let trElem = document.getElementsByTagName("tr");
        for (let i = trElem.length - 1; i > 0; i--) {
            trElem[i].remove();
        }
    }

    function initCalendar() {
        var calendarEl = document.getElementById('calendar');

        calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            initialDate: '2022-06-06',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: ''
            },
            eventSources: [
                {
                    url: 'http://10.92.198.27:8080/event',
                    color: 'yellow',
                    textColor: 'black'
                }
                

            ],
            events: [],
             eventClick: function(info) {

                alert('Event: ' + info.event.title + info.event.periodo);



                // change the border color just for fun

                info.el.style.borderColor = 'red';

            }  

        });


        calendar.render();
    }
    // http://10.92.198.20:8080/

    let fetchData = () => {
        let url = `http://10.92.198.27:8080/event `

        fetch(url)
            .then(response => response.json())
            .then(fetchData => {
                console.log(fetchData)
            })
    }
    fetchData()
}

