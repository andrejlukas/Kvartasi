import { Navbar } from "../../components/Navbar";
import { useEffect, useState } from 'react';
import '../../styles/Kosarica.css'


export function Kosarica(){
    const [emailAddress,setEmailAddress] = useState("")
    const [id,setId] = useState(null)
    const [error,setError]=useState("")

    const [kosarica,setKosarica] = useState({})

    const [loading, setLoading]=useState(true);
        

    function uvecajKolicinuProizvoda(proizvodID) {
        const token = localStorage.getItem("token");
        if (!token) {
            setError("Nedostaje token za autorizaciju.");
            return;
        }
    
        // Provjera potrebnih varijabli
        if (!id || !proizvodID) {
            setError("Podaci za kupca ili proizvod nisu dostupni.");
            console.log("ID kupca:", id);
            console.log("ID proizvoda:", proizvodID);
            return;
        }
    
        const options = {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        };
    
        // API poziv za povećanje količine
        fetch(`/api/kupacProizvods/povecaj/${id}/${proizvodID}`, options)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Greška prilikom povećanja količine.");
                }
            })
            .then(() => {
                // Ponovno dohvaćanje košarice nakon uspješnog ažuriranja
                const fetchOptions = {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                };
    
                fetch(`/api/kupacProizvods/kosarica/${id}`, fetchOptions)
                    .then((response) => {
                        if (!response.ok) {
                            throw new Error("Greška prilikom dohvaćanja košarice.");
                        }
                        return response.json();
                    })
                    .then((data) => {
                        setKosarica(data); // Ažuriranje košarice u state
                        console.log("Ažurirana košarica:", data);
                    })
                    .catch((error) => {
                        console.error("Greška prilikom dohvaćanja košarice:", error);
                        setError(error.message);
                    });
            })
            .catch((error) => {
                console.error("Greška prilikom povećanja količine:", error);
            });
    }
    
    function smanjiKolicinuProizvoda (proizvodID) {

        const token = localStorage.getItem("token");
        if (!token) {
            setError("Nedostaje token za autorizaciju.");
            return;
        }
    
        // Provjera potrebnih varijabli
        if (!id || !proizvodID) {
            setError("Podaci za kupca ili proizvod nisu dostupni.");
            console.log("ID kupca:", id);
            console.log("ID proizvoda:", proizvodID);
            return;
        }
    
        const options = {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        };
    
        // API poziv za povećanje količine
        fetch(`/api/kupacProizvods/smanji/${id}/${proizvodID}`, options)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Greška prilikom smanjivanja količine.");
                }
            })
            .then(() => {
                // Ponovno dohvaćanje košarice nakon uspješnog ažuriranja
                const fetchOptions = {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                };
    
                fetch(`/api/kupacProizvods/kosarica/${id}`, fetchOptions)
                    .then((response) => {
                        if (!response.ok) {
                            throw new Error("Greška prilikom dohvaćanja košarice.");
                        }
                        return response.json();
                    })
                    .then((data) => {
                        setKosarica(data); // Ažuriranje košarice u state
                        console.log("Ažurirana košarica:", data);
                    })
                    .catch((error) => {
                        console.error("Greška prilikom dohvaćanja košarice:", error);
                        setError(error.message);
                    });
            })
            .catch((error) => {
                console.error("Greška prilikom smanjivanja količine:", error);
            });
        
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
      setLoading(true); 


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
            })
            .finally(() => {
                setLoading(false); 
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
                {
                    loading ? (
                        <p>Loading...</p>
                    ) :
                    (
                        <div className="kosarica-glavni-div">
                    <div className="kosarica-odabir">
                        <ul>
                            <li><a id = "kosarica-odabir"href="/kosarica">Moja košarica</a></li>
                            <li><a href= "/mojeponudeipromocije">Moje ponude i promocije</a></li>
                        </ul>
                    </div>
                    <div className="prikaz-kosarice">
                    {
                        (kosarica && Object.keys(kosarica).length > 0) ? (
                            Object.entries(kosarica).map(([key, proizvodi]) => (
                                
                                /* console.log(key) */
                                <div className={key} id="kosarica-element" >
                                    
                                    <p className="kosarica-element-naslov">{proizvodi[0].trgovinaNaziv}</p>
                                    <hr></hr>
                                    <div className="kosarica-proizvodi-ispis" >
                                        {
                                            proizvodi
                                            .slice() 
                                            .sort((a, b) => a.proizvodNaziv.localeCompare(b.proizvodNaziv))
                                            .map((proizvod)=>
                                                <div className={proizvod.proizvodNaziv} id="kosarica-proizvod">
                                                <p id="nazproiz">{proizvod.proizvodNaziv}</p> 
                                                <div className="kosarica-proizvod-detalji">
                                                    <div id="element-proizvoda" className="kosarica-proizvod-cijena">
                                                        <p id="cijj">{proizvod.proizvodCijena.toFixed(2)}€/kom</p>
                                                    </div>
                                                    <div id="element-proizvoda" className="kosarica-proizvod-kolicina">
                                                        <button onClick={() => smanjiKolicinuProizvoda(proizvod.proizvodId)}>-</button>
                                                        <p>{proizvod.proizvodKolicina}</p>
                                                        <button onClick={() => uvecajKolicinuProizvoda(proizvod.proizvodId)}>+</button>
                                                    </div>
                                                    <p id="element-proizvoda" className="proizvod-kolicina-iznos">
                                                        {(proizvod.proizvodCijena * proizvod.proizvodKolicina).toFixed(2)}€
                                                    </p>
                                                </div>
                                            </div>

                                            )
                                        }
                                    </div>
                                    <div className="trgovina-ukupno">
                                        <p>Ukupno:  </p>
                                        <p id="ukupcijena">{
                                        proizvodi.reduce(
                                            (sum, proizvod) =>
                                                sum + proizvod.proizvodKolicina * proizvod.proizvodCijena,
                                            0
                                            
                                        ).toFixed(2)
                                        } €</p>
                                    </div>
                                        
                                </div>
                            )
                            )
                        ) : (
                            <p>Kosarica je prazna.</p>
                        )
                    }

                    </div>
                    
                </div>
                    )
                }
                
            </div>
        </div>
    )
    
}