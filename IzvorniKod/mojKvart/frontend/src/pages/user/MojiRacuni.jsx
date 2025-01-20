import { Navbar } from "../../components/Navbar";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { MapContainer, TileLayer, Marker, useMapEvents  } from 'react-leaflet';
import "leaflet/dist/leaflet.css";
import "../../styles/MojiRacuni.css"; 

export function MojiRacuni() {
    
    const [racuni,setRacuni] = useState({})
    const [trgovine,setTrgovine] = useState([])

    //recenzija
    const [tekstRec,setTekstRec] = useState([])
    const [ocjenaRec,setOcjenaRec] = useState([])
    const [trgovinaRec,setTrgovinaRec] = useState("")

    const [racuniInfo,setRacuniInfo] = useState({})
    const [error,setError] = useState(null)

    const [errorMessage,setErrorMessage] = useState([])

    //potrebno za ispis proizvoda u računu
    const [openedRacuni, setOpenedRacuni] = useState([]); // Čuva ID-ove otvorenih računa
    const [openedRecenzije, setOpenedRecenzije] = useState([]); // Čuva ID-ove računa otvorenih recenzija

    const [loading, setLoading]=useState(true);


    const renderStars = (key) => {
        const stars = [];
        const currentRating = ocjenaRec[key] || 1;
        const [hover, setHover] = useState(0);

        for (let i = 1; i <= 5; i++) {
            stars.push(
                <span
                    key={i}
                    className={`star ${i <= (hover || currentRating) ? 'filled' : ''}`}
                    onClick={() => setOcjenaRec(prev => ({ ...prev, [key]: i }))}
                    onMouseEnter={() => setHover(i)}
                    onMouseLeave={() => setHover(0)}
                    role="button"
                    aria-label={`Ocjena ${i}`}
                    tabIndex={0}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            setOcjenaRec(prev => ({ ...prev, [key]: i }));
                        }
                    }}
                >
                    &#9733;
                </span>
            );
        }
        return stars;
    };

    const prikazracuna = (key) => {
        if (openedRacuni.includes(key)) {
            // Ako je već otvoren, ukloni ga iz niza
            setOpenedRacuni(openedRacuni.filter((id) => id !== key));
            setOpenedRecenzije(openedRacuni.filter((id) => id !== key));
            setTekstRec(prevNames => ({ ...prevNames, [key]: "" }))
            setOcjenaRec(prevNames => ({ ...prevNames, [key]: 1 }))
            setErrorMessage((prevNames => ({ ...prevNames, [key]: "" })));

        } else {
            // Ako nije otvoren, dodaj ga u niz
            setOpenedRacuni([...openedRacuni, key]);
        }
    };

    const ostaviRecenziju = (key) => {
        if (openedRecenzije.includes(key)) {
            // Ako je već otvoren, ukloni ga iz niza
            setOpenedRecenzije(openedRacuni.filter((id) => id !== key));
            setTekstRec(prevNames => ({ ...prevNames, [key]: "" }))
            setOcjenaRec(prevNames => ({ ...prevNames, [key]: 1 }))
            setErrorMessage((prevNames => ({ ...prevNames, [key]: "" })));
            
        } else {
            // Ako nije otvoren, dodaj ga u niz
            setOpenedRecenzije([...openedRacuni, key]);
        }
    };

    function dodajRec (e,poslaID,racunIdPoslan)

    {
        e.preventDefault();
        if (!ocjenaRec[racunIdPoslan] || !tekstRec[racunIdPoslan]){
            

            
            setErrorMessage((prevNames => ({ ...prevNames, [racunIdPoslan]: "Sve polja moraju biti popunjena" })));
            return;
        }
        else if(ocjenaRec[racunIdPoslan] > 5 || ocjenaRec[racunIdPoslan] < 1){
            setErrorMessage((prevNames => ({ ...prevNames, [racunIdPoslan]: "Ocjena mora biti od 1 do 5" })));

        }
        else{
            setErrorMessage((prevNames => ({ ...prevNames, [racunIdPoslan]: "" })));

        }

        
            

        const token = localStorage.getItem('token');
            if (!token || !poslaID || !ocjenaRec[racunIdPoslan] || !tekstRec[racunIdPoslan] ) {
                
                
                throw new Error("Podaci za recenziju nisu dostupni");
              }
          
            const options4 = {
                method: "POST",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    odobrioModerator: false,
                    recenzijaOdgovor: null, 
                    vrijemeKreiranja: null,
                    recenzijaZvjezdice: ocjenaRec[racunIdPoslan] ,
                    recenzijaOpis: tekstRec[racunIdPoslan],
                    kupacId: id,
                    trgovinaId:poslaID,

                  }),
              };
            fetch(`/api/recenzijas`, options4)
            .then(response => !response.ok && Promise.reject('Failed to save changes'))
            .then(updated => {
                setTekstRec(prevNames => ({ ...prevNames, [racunIdPoslan]: "" }))
                setOcjenaRec(prevNames => ({ ...prevNames, [racunIdPoslan]: 1 }))
            })
            .catch(error => {
                console.error('Error updating data:', error);
            })

       

    }






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

    fetch('/api/tokens/claims', options)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
          setEmailAddress(data.email);
          //console.log(data)
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
        setLoading(true); 
        
        // Dohvaćanje raucna preko id-a
        //console.log(id)
        if (id) {
            fetch(`api/kupacProizvods/prosleNarudzbe/${id}`, options)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    setRacuni(data)
                    Object.keys(data).forEach((key) => fetchRacunInfo(key));

                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation: ', error);
                    setError(error.message);
                })
                .finally(() => {
                    setLoading(false); 
                 });
        }


        const fetchRacunInfo = async (racunId) => {
            try {
               const options3 = {
                  method: 'GET',
                  headers: {
                  'Authorization': `Bearer ${token}`,
                  'Content-Type': 'application/json',
                  },
               };
               const response = await fetch(`/api/racuns/${racunId}`, options3);
               if (!response.ok) {
                  throw new Error(`Failed to fetch racun with id ${racunId}`);
               }
               const name = await response.json();
               //console.log(name)
               setRacuniInfo(prevNames => ({ ...prevNames, [racunId]: [name.trgovina,name.vrijemeDatumNastanka] }));
            } catch (error) {
               console.error(`Error fetching store name for id ${racunId}:`, error);
            }
         };
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
                    { loading ? (<p>Loading...</p>)
                        
                        :(
                        
                            <div className="moji-racuni-container">
                                <h1>Moji racuni</h1>
                                <div className="moji-racuni-lista">
                                    {
                                        Object.keys(racuni).length > 0 ? (Object.keys(racuni).map((key) => (
                                            //iteriram po ključevima(idračuna)
                                            
                                            <div key={key} className="racun-element">
                                               {
                                                Object.entries(racuniInfo).map(([key2, podaci]) =>
                                                    key2 === key ? (
                                                    <button
                                                        key={key2} // Obavezno dodajte jedinstveni `key` za svaki element
                                                        className="idracun-button"
                                                        onClick={() => prikazracuna(key)}
                                                    >
                                                        Račun: {key} : {racuni[key][0].trgovinaNaziv} : {racuniInfo[key][1]}
                                                    </button>
                                                    ) : null // Ako uvjet nije ispunjen, vraća `null` (ništa)
                                                )
                                                }
                                                
                                                
                                                {openedRacuni.includes(key) && <div className="ispisRacuna">

                                                    <button className="racun-ostavi-recenziju-gumb" onClick={() => ostaviRecenziju(key)} >Ostavi recenziju!</button>
                                                    {
                                                        openedRecenzije.includes(key) && 
                                                        (
                                                            <div className="ostavljanje-recenzije-container">
                                                                <form id ="unos-recenziije-form" onSubmit={(e) => dodajRec(e, racuniInfo[key][0],key)} >
                                                                <div className="div-unos-tekst-recenzija">
                                                                    <label >Unesite tekst:</label>
                                                                    <input id="tekstrecenzija-mojiracuni"
                                                                        type="text"
                                                                        placeholder={tekstRec[key]}
                                                                        value={tekstRec[key] }
                                                                        onChange={(e) => setTekstRec(prevNames => ({ ...prevNames, [key]: e.target.value }))}
                                                                        
                                                                    />
                                                                </div>
                                
                                                                <div>
                                                                    <label >Ocjeni:</label>
                                                                    <div className="star-rating">
                                                                    {[...Array(5)].map((star, index) => {
                                                                        const starValue = index + 1;
                                                                        return (
                                                                            <span
                                                                                key={starValue}
                                                                                className={`star ${starValue <= (ocjenaRec[key] || 1) ? 'filled' : ''}`}
                                                                                onClick={() => setOcjenaRec(prev => ({ ...prev, [key]: starValue }))}
                                                                                onMouseEnter={() => setOcjenaRec(prev => ({ ...prev, [`hover-${key}`]: starValue }))}
                                                                                onMouseLeave={() => setOcjenaRec(prev => ({ ...prev, [`hover-${key}`]: 0 }))}
                                                                                role="button"
                                                                                aria-label={`Ocjena ${starValue}`}
                                                                                tabIndex={0}
                                                                                onKeyDown={(e) => {
                                                                                    if (e.key === 'Enter') {
                                                                                        setOcjenaRec(prev => ({ ...prev, [key]: starValue }));
                                                                                    }
                                                                                }}
                                                                            >
                                                                                &#9733;
                                                                            </span>
                                                                        );
                                                                    })}

                                                                    </div>
                                                                    {/* <input
                                                                        type="number"
                                                                        value={ocjenaRec[key]}
                                                                        placeholder={ocjenaRec[key] || Number(1)}

                                                                        onChange={(e) => setOcjenaRec(prevNames => ({ ...prevNames, [key]: Number(e.target.value) }))}
                                                                        
                                                                    /> */}
                                                                </div>
                                                                { Object.keys(errorMessage).includes(key) && <p style={{ color: 'red' }}>{errorMessage[key]}</p>}
                                                                <div className="form-buttons">
                                                                    <button  type="submit" >Predaj</button>
                                                                </div>
                                
                                                                </form>
                                                                
                                                            </div>
                                
                                                            
                                                        )
                                                    }
                                                    <hr></hr>
                                                    <div className="racun-dopunski-tekst">
                                                        <p>Proizvod</p>
                                                        <p>Cijena</p>
                                                        <p>Kolicina</p>
                                                        <p>Ukupna cijena:</p>
                
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