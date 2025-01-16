import { Navbar } from "../../components/Navbar";
import { useEffect, useState } from 'react';
import '../../styles/Kosarica.css'


export function Kosarica(){
    const [emailAddress,setEmailAddress] = useState("")
    const [id,setId] = useState(null)
    const [error,setError]=useState("")

    const [kosarica,setKosarica] = useState({})

    function uvecajKolicinuProizvoda  (proizvodID) {

        const token = localStorage.getItem("token");
        if (!token) {
          setError("Nedostaje token za autorizaciju.");
          return;
        }
      
        // Provjerite jesu li potrebne varijable postavljene
        if (!id || !proizvodID) {
          setError("Podaci za kupca, trgovinu ili proizvod nisu dostupni.");
          console.log(id)
          console.log(proizvodID)
    
          return;
        }
      
    
    
        const options = {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        };
      
        
          fetch(`/api/kupacProizvods/povecaj/${id}/${proizvodID}`, options)
        .then(response => response.ok && console.log("uspjela smanjit"))
        .then(updated => {
          console.log(updated)
        })
        .catch(error => {
            console.error('Error updating data:', error);
        })
        window.location.reload();

      
        
    }
    function smanjiKolicinuProizvoda (proizvodID) {

        const token = localStorage.getItem("token");
        if (!token) {
          setError("Nedostaje token za autorizaciju.");
          return;
        }
      
        // Provjerite jesu li potrebne varijable postavljene
        if (!id || !proizvodID) {
          setError("Podaci za kupca, trgovinu ili proizvod nisu dostupni.");
          console.log(id)
          console.log(proizvodID)
    
          return;
        }
      
    
    
        const options = {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        };
      
        
          fetch(`/api/kupacProizvods/smanji/${id}/${proizvodID}`, options)
        .then(response => response.ok && console.log("uspjela smanjit"))
        .then(updated => {
          console.log(updated)
        })
        .catch(error => {
            console.error('Error updating data:', error);
        })
        window.location.reload();
      
        
    }

    //dohvacanje mail-a
   useEffect(() => {
    const token = localStorage.getItem('token');
    var options = {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ oneLiner: token })
    }

    fetch('/api/tokens/claims', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
          setEmailAddress(data.email);
        })
        .catch(error => console.error('There was a problem with the fetch operation: ', error));
    }, []); //prazno, radi se na pocetku samo

    useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };

 // Dohvaćanje korisničkih podataka ako postoji emaila
 if (emailAddress) {
     fetch(`/api/kupacs/${emailAddress}`, options)
         .then(response => {
             if (!response.ok) {
                 throw new Error('Network response was not ok');
             }
             return response.json();
         })
         .then(data => {
             setId(data.kupacId);
             console.log(token)
         })
         .catch(error => {
             console.error('There was a problem with the fetch operation: ', error);
             setError(error.message);
         });
 }
}, [emailAddress]);
    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

    // Dohvaćanje korisničkih podataka ako postoji emaila
    if (emailAddress) {
        fetch(`/api/kupacs/${emailAddress}`, options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setId(data.kupacId);
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation: ', error);
                setError(error.message);
            });
    }
    }, [emailAddress]);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        };

    // Dohvaćanje korisničkih podataka ako postoji emaila
    if (emailAddress) {
        fetch(`/api/kupacProizvods/kosarica/${id}`, options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                setKosarica(data);
                console.log(data)
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation: ', error);
                setError(error.message);
            });
    }
    }, [id]);

    useEffect(() => console.log(kosarica), [kosarica])

    return(
        <div className="kosarica-wrappper">
            <Navbar/>
            <div className="main-container">
                <div className="kosarica-glavni-div">
                    <div className="kosarica-odabir">
                        <ul>
                            <li><a id = "kosarica-odabir"href="/kosarica">Moja košarica</a></li>
                            <li><a href= "/mojeponudeipromocije">Moje ponude i promocije</a></li>
                        </ul>
                    </div>
                    <div className="prikaz-kosarice">
                    {
                        (kosarica) ? (
                            Object.entries(kosarica).map(([key, proizvodi]) => (
                                
                                /* console.log(key) */
                                <div className={key} >
                                    
                                    <h3>Proizvodi : {proizvodi[0].trgovinaNaziv}</h3>
                                    <div className="kosarica-proizvodi-ispis">
                                        {
                                            proizvodi.map((proizvod)=>
                                            <div className={proizvod.proizvodNaziv}  id="kosarica-proizvod">
                                                <p>{proizvod.proizvodNaziv}</p>
                                                <button onClick={() =>smanjiKolicinuProizvoda(proizvod.proizvodId)}>-</button>
                                                <p>Kol: {proizvod.proizvodKolicina}</p>
                                                <button onClick={() => uvecajKolicinuProizvoda(proizvod.proizvodId)}>+</button>
                                                <p>Cijena: {proizvod.proizvodCijena}</p>
                                                
                                            </div>
                                            )
                                        }
                                    </div>
                                    <p>ukupno novca za ovu trgovinu: {
                                        proizvodi.reduce(
                                            (sum, proizvod) =>
                                                sum + proizvod.proizvodKolicina * proizvod.proizvodCijena,
                                            0
                                        )
                                        }</p>
                                </div>
                            )
                            )
                        ) : (
                            <div>Kosarica je prazna.</div>
                        )
                    }

                    </div>
                    
                </div>
            </div>
        </div>
    )
    
}