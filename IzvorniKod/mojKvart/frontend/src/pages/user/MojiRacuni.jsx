import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents  } from 'react-leaflet';
import "leaflet/dist/leaflet.css";
import "../../styles/MojiRacuni.css"; 

export function MojiRacuni() {
    
    const [racuni,setRacuni] = useState({})
    const [ostavljanjeRec,setOstavljanjeRec] = useState(false)
    const [trgovine,setTrgovine] = useState([])

    //recenzija
    const [tekstRec,setTekstRec] = useState("")
    const [ocjenaRec,setOcjenaRec] = useState(null)
    const [trgovinaRec,setTrgovinaRec] = useState("")

    //potrebno za ispis proizvoda u računu
    const [openedRacuni, setOpenedRacuni] = useState([]); // Čuva ID-ove otvorenih računa

    const prikazracuna = (key) => {
        if (openedRacuni.includes(key)) {
            // Ako je već otvoren, ukloni ga iz niza
            setOpenedRacuni(openedRacuni.filter((id) => id !== key));
        } else {
            // Ako nije otvoren, dodaj ga u niz
            setOpenedRacuni([...openedRacuni, key]);
        }
    };

    const ostaviRecenziju = () => {
        setOstavljanjeRec(true);
    };

    






    //dohvacanje racuna - imam samo id kupca
    const [id, setId] = useState("")
    const [emailAddress, setEmailAddress] = useState('')

       

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
    console.log(token)

    fetch('/api/tokens/claims', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
          setEmailAddress(data.email);
          console.log(data)
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
     
        // Dohvaćanje recenzija preko id-a
        console.log(id)
        if (id) {
            fetch(`api/kupacProizvods/prosleNarudzbe/${id}`, options)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log(data)
                    setRacuni(data)
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation: ', error);
                    setError(error.message);
                });
        }
     }, [id]);

     // koristit
        /*  <button onClick={handleToggle}>
        {showDiv ? "Ukloni tekst" : "Dodaj tekst"}
    </button>
    {showDiv && <div className="textDiv">Ovo je tekst unutar stvorenog diva.</div>}
    */
   return (
      <div className="moji-racuni-wrapper">

         <Navbar/>
        
         <div className="main-container">
                    {
                        ostavljanjeRec ? 
                        (
                            <div className="ostavljanje-recenzije-container">
                                <h1>Ostavi recenziju</h1>
                                <form id ="unos-recenziije-form" >
                                <div>
                                    <label >Unesite tekst:</label>
                                    <input
                                        type="text"
                                        placeholder={tekstRec}
                                        value={tekstRec}
                                        onChange={(e) => setTekstRec(e.target.value)}
                                        required
                                    />
                                </div>

                                <div>
                                    <label>Odaberite trgovinu:</label>
                                    <select
                                        value={trgovinaRec}
                                        onChange={(e) => setTrgovinaRec(e.target.value)}
                                        required
                                    >
                                        <option value="">Odaberite...</option>
                                        {
                                            
                                            /* option sve trgovine iz racuna */
                                            

                                        }
                                        
                                    </select>
                                </div>

                                <div>
                                    <label htmlFor="numberInput">Unesite broj (1-5):</label>
                                    <input
                                        type="number"
                                        min="1"
                                        max="5"
                                        value={ocjenaRec}
                                        onChange={(e) => setOcjenaRec(Number(e.target.value))}
                                        required
                                    />
                                </div>

                                <div className="form-buttons">
                                    <button /* onClick={dodajRec} */ >Predaj</button>
                                    <button  /* onClick={zatvoriDodavanjeRec} */>
                                        Zatvori
                                    </button>
                                </div>

                                </form>
                                
                            </div>

                            
                        )
                        :
                        (
                            <div className="moji-racuni-container">
                                <h1>Moji racuni</h1>
                                <div className="moji-racuni-lista">
                                    {
                                        Object.keys(racuni).length > 0 ? (Object.keys(racuni).map((key) => (
                                            //iteriram po ključevima(idračuna)
                                            
                                            <div key={key} className="racun-element">
                
                                                
                                                    <button className="idracun-button"onClick={() => prikazracuna(key)}>Račun: {key} </button>
                                                {openedRacuni.includes(key) && <div className="ispisRacuna">

                                                    <button className="racun-ostavi-recenziju-gumb" onClick={() => ostaviRecenziju()} >Ostavi recenziju!</button>
                                                    <hr></hr>
                                                    <div className="racun-dopunski-tekst">
                                                        <p>Proizvod</p>
                                                        <p>Cijena</p>
                                                        <p>Kolicina</p>
                                                        <p>Ukupna cijena:</p>
                                                        <p>Trgovina</p>
                
                                                    </div >
                                                    
                                                    {
                                                         // Dohvaća vrijednost ključa (niz proizvoda)
                                                        racuni[key].map((proizvod) => (
                                                            /* console.log(`Proizvod: ${proizvod.proizvodNaziv}`);
                                                            console.log(`Cijena: ${proizvod.proizvodCijena}`);
                                                            console.log(`Trgovina: ${proizvod.proizvodKolicina}`); */
                                                            <div key={proizvod.proizvodNaziv} className="racun-proizvod-element">
                                                                <p>{proizvod.proizvodNaziv}</p>
                                                                <p>{proizvod.proizvodCijena}</p>
                                                                <p>{proizvod.proizvodKolicina}</p>
                                                                <p>{proizvod.proizvodKolicina * proizvod.proizvodCijena}</p>
                                                                <p>{proizvod.trgovinaNaziv}</p>
                                                                
                                                            </div>
                                                            
                                                        )
                                                        )
                                                    }
                                                    
                                                    
                                                </div>}
                                            </div>
                                        )))    
                                    :
                                    (<p>Korisnik nema račune</p>)
                                    }

                                </div>
                            </div>
                            
                                
                        )
                    }
                
            </div>
      </div>
   );
}