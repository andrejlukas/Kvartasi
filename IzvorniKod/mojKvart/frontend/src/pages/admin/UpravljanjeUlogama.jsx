import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/AdminNavbar'
// import "../../styles/Home.css";
import "../../styles/UpravljanjeUlogama.css";

import "bootstrap/dist/css/bootstrap.min.css";

export function AdminUpravljanjeUlogama(){
    const [korisniciTip,setKorisniciTip] = useState("kupac")
    const [korisniciStanje, setKorisniciStanje] = useState("verificirani"); 
    const [error,setError] = useState("")
    
    const handlekorisniciTipChange = (e) => {
        setKorisniciTip(e.target.value);
    };
    const handlekorisniciStanjeChange = (e) => {
        setKorisniciStanje(e.target.value);
    };

    const [trgovinas, setTrgovinas] = useState([]);
    const [kupacs, setKupacs] = useState([]);
    const [moderators, setModeratos] = useState([]);
    const [Ntrgovinas, setNTrgovinas] = useState([]);
    const [Nkupacs, setNKupacs] = useState([]);
    const [Nmoderators, setNModeratos] = useState([]);
    const [prikaz, setPrikaz] = useState([]);

    function verificirajKorisnika(customerData,tip) {
        const token = localStorage.getItem("token");
        if(tip == "kupac"){
            const options = {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...customerData, kupacStatus: "V" }),
              };
              fetch(`/api/kupacs/${customerData.kupacId}`, options)
              .then(async (response) => {
                if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
                }
              })
              .then(() => {
                fetchData("/api/kupacs/verificirani", setKupacs);
                fetchData("/api/kupacs/neverificirani", setNKupacs);
              })
              .catch((error) => {
                setError(error.message);
              });
        }
        else if(tip == "trgovina"){
            const options = {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...customerData, trgovinaStatus: "V" }),
              };
              console.log(customerData)

              fetch(`/api/trgovinas/${customerData.trgovinaId}`, options)
              .then(async (response) => {
                if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
                }
              })
              .then(() => {
                fetchData("/api/trgovinas/verified", setTrgovinas);
                fetchData("/api/trgovinas/notverified", setNTrgovinas);
                
              })
              .catch((error) => {
                setError(error.message);
              });
        }
        else{
            const options = {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...customerData, moderatorStatus: "V" }),
              };
              fetch(`/api/moderators/${customerData.moderatorId}`, options)
              .then(async (response) => {
                if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
                }
              })
              .then(() => {
                fetchData("/api/moderators/verified", setModeratos);
                fetchData("/api/moderators/notverified", setNModeratos);
              })
              .catch((error) => {
                setError(error.message);
              });

        }
      }

    const fetchData = async (url, setStateFunction) => {
            const token = localStorage.getItem("token");
            console.log(token)
            const options = {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
            };
    
            try {
                const response = await fetch(url, options);
    
                if (!response.ok) {
                    throw new Error(`HTTP greška! Status: ${response.status}`);
                }
    
                const contentType = response.headers.get("content-type");
                if (!contentType || !contentType.includes("application/json")) {
                    const text = await response.text();
                    throw new Error(`Odgovor nije validan JSON: ${text}`);
                }
    
                const data = await response.json();
                setStateFunction(data);
            } catch (error) {
                setError(error.message);
            }
        };
    
        // Učitavanje korisnika pri pokretanju
    useEffect(() => {
        fetchData("/api/kupacs/verificirani", setKupacs);
        fetchData("/api/kupacs/neverificirani", setNKupacs);
        fetchData("/api/trgovinas/verified", setTrgovinas);
        fetchData("/api/trgovinas/notverified", setNTrgovinas);
        fetchData("/api/moderators/verified", setModeratos);
        fetchData("/api/moderators/notverified", setNModeratos);
    }, []);

    useEffect(() => {
            let korisnici;
            if (korisniciTip === "kupac") {
                korisnici = korisniciStanje === "neverificirani" ? Nkupacs : kupacs;
            } else if (korisniciTip === "trgovina") {
                korisnici = korisniciStanje === "neverificirani" ? Ntrgovinas : trgovinas;
            } else if (korisniciTip === "moderator") {
                korisnici = korisniciStanje === "neverificirani" ?Nmoderators : moderators;
            }
    
            setPrikaz(korisnici);
        }, [korisniciTip, korisniciStanje, Nkupacs, kupacs, Ntrgovinas, trgovinas, Nmoderators, moderators]);

    return <div>
        <Navbar />
        <h1>Potvrđivanje uloga</h1>
        <div className="upravljanje-ulogama-popis-div">
        <div id="customerController1" className="odabirKorisnika">
                <label>
                    <input
                        className="odabirTipa"
                        type="radio"
                        name="korisniciTip"
                        value="kupac"
                        checked={korisniciTip === "kupac"}
                        onChange={handlekorisniciTipChange}
                    />
                    Kupci
                </label>
                <label>
                    <input
                        className="odabirTipa"
                        type="radio"
                        name="korisniciTip"
                        value="trgovina"
                        checked={korisniciTip === "trgovina"}
                        onChange={handlekorisniciTipChange}
                    />
                    Trgovine
                </label>
                <label>
                    <input
                        className="odabirTipa"
                        type="radio"
                        name="korisniciTip"
                        value="moderator"
                        checked={korisniciTip === "moderator"}
                        onChange={handlekorisniciTipChange}
                    />
                    Moderatori
                </label>
        </div>
        <div id="customerController1">
                 <label>
                    <input
                        type="radio"
                        name="korisniciStanje"
                        value="verifiicirani"
                        checked={korisniciStanje === "verifiicirani"}
                        onChange={handlekorisniciStanjeChange}
                    />
                    Verificirani
                </label>
                <label>
                    <input
                        type="radio"
                        name="korisniciStanje"
                        value="neverificirani"
                        checked={korisniciStanje === "neverificirani"}
                        onChange={handlekorisniciStanjeChange}
                    />
                    Neverificirani
                </label>
        </div>
        <div className="row" >
                {error && <p style={{ color: "red" }}>Greška: {error}</p>}
                {prikaz.length > 0 ? (
                    prikaz.map((korisnik) => (
                        <div key={korisnik.kupacId || korisnik.trgovinaId || korisnik.moderatorId} className="col-lg-4 col-md-6 col-12 mb-3">
                            <div className="card" id="sus-card">
                                <div className="card-body">
                                    <h5 className="card-title">ID: {korisnik.kupacId || korisnik.trgovinaId || korisnik.moderatorId}</h5>
                                    <p><strong>Email:</strong> {korisnik.kupacEmail || korisnik.trgovinaEmail || korisnik.moderatorEmail}</p>
                                    {
                                        korisniciTip =="trgovina" && <p><strong>Naziv:</strong> {korisnik.trgovinaNaziv} </p>
                                    }
                                    {
                                        (korisniciTip == "kupac" || korisniciTip=="moderator") && (<p><strong>Ime:</strong> {korisnik.kupacIme || korisnik.moderatorIme}</p>)
                                    }
                                    {
                                        (korisniciTip == "kupac" || korisniciTip=="moderator") && (<p><strong>Prezime :</strong> {korisnik.kupacPrezime || korisnik.moderatorPrezime}</p>)
                                    }
                                    
                                    {korisniciStanje === "neverificirani" && (
                                        <button 
                                        className="verificiraj-korisnika-button"
                                        onClick={() => verificirajKorisnika(korisnik,korisniciTip)}
                                        >
                                        Potvrdi {korisniciTip =="kupac" && "kupca"} {korisniciTip =="trgovina" && "trgovinu"} {korisniciTip =="moderator" && "moderatora"}
                                        </button>
                                    )}
                                    
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>Nema korisnika u ovoj kategoriji.</p>
                )}
            </div>

        </div>
        
    </div>
}