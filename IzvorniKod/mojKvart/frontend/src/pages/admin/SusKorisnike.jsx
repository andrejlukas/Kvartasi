import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/AdminNavbar';
import "bootstrap/dist/css/bootstrap.min.css";

export function SusKorisnike() {
    const [trgovinas, setTrgovinas] = useState([]);
    const [kupacs, setKupacs] = useState([]);
    const [moderators, setModeratos] = useState([]);
    const [prikaz, setPrikaz] = useState([]);

    const [Strgovinas, setSTrgovinas] = useState([]);
    const [Skupacs, setSKupacs] = useState([]);
    const [Smoderators, setSModeratos] = useState([]);

    const [error, setError] = useState(null);

    const [korisniciStanje, setKorisniciStanje] = useState("suspended"); // može biti "nonSuspended"
    const [korisniciTip, setKorisniciTip] = useState("kupac"); // može biti "moderator", "trgovina"


    function odsuspendirajKorisnika(customerData,tip){
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
                fetchData("/api/kupacs/suspendirani", setSKupacs);
              })
              .catch((error) => {
                setError(error.message);
              });
        }
        else if(tip == "trgovina"){
            console.log(customerData)

            const options = {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...customerData, trgovinaStatus: "V" }),
              };

              fetch(`/api/trgovinas/${customerData.trgovinaId}`, options)
              .then(async (response) => {
                if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
                }
              })
              .then(() => {
                fetchData("/api/trgovinas/verified", setTrgovinas);
                fetchData("/api/trgovinas/suspended", setSTrgovinas);
                
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
              console.log(customerData)

              fetch(`/api/moderators/${customerData.moderatorId}`, options)
              .then(async (response) => {
                if (!response.ok) {
                  const text = await response.text();
                  throw new Error(text);
                }
              })
              .then(() => {
                fetchData("/api/moderators/verified", setModeratos);
                fetchData("/api/moderators/suspended", setSModeratos);
              })
              .catch((error) => {
                setError(error.message);
              });

        }
    }

    function suspendirajKorisnika(customerData,tip) {
        const token = localStorage.getItem("token");
        if(tip == "kupac"){
            const options = {
                method: "PUT",
                headers: {
                  Authorization: `Bearer ${token}`,
                  "Content-Type": "application/json",
                },
                body: JSON.stringify({ ...customerData, kupacStatus: "S" }),
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
                fetchData("/api/kupacs/suspendirani", setSKupacs);
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
                body: JSON.stringify({ ...customerData, trgovinaStatus: "S" }),
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
                fetchData("/api/trgovinas/suspended", setSTrgovinas);
                
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
                body: JSON.stringify({ ...customerData, moderatorStatus: "S" }),
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
                fetchData("/api/moderators/suspended", setSModeratos);
              })
              .catch((error) => {
                setError(error.message);
              });

        }
        
    
        
      }

    
    const handlekorisniciTipChange = (e) => {
        setKorisniciTip(e.target.value);
    };


    const handlekorisniciStanjeChange = (e) => {
        setKorisniciStanje(e.target.value);
    };

    
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
        fetchData("/api/kupacs/suspendirani", setSKupacs);
        fetchData("/api/trgovinas/verified", setTrgovinas);
        fetchData("/api/trgovinas/suspended", setSTrgovinas);
        fetchData("/api/moderators/verified", setModeratos);
        fetchData("/api/moderators/suspended", setSModeratos);
    }, []);

    // useEffect za filtriranje korisnika na osnovu izbora
    useEffect(() => {
        let korisnici;
        if (korisniciTip === "kupac") {
            korisnici = korisniciStanje === "suspended" ? Skupacs : kupacs;
        } else if (korisniciTip === "trgovina") {
            korisnici = korisniciStanje === "suspended" ? Strgovinas : trgovinas;
        } else if (korisniciTip === "moderator") {
            korisnici = korisniciStanje === "suspended" ? Smoderators : moderators;
        }

        setPrikaz(korisnici);
    }, [korisniciTip, korisniciStanje, Skupacs, kupacs, Strgovinas, trgovinas, Smoderators, moderators]);

    return (
        <div>
            <Navbar />
            <h1>Pregled korisnika</h1>

            <div id="customerController1">
                <label>
                    <input
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
                        value="suspended"
                        checked={korisniciStanje === "suspended"}
                        onChange={handlekorisniciStanjeChange}
                    />
                    Suspendirani
                </label>
                <label>
                    <input
                        type="radio"
                        name="korisniciStanje"
                        value="nonSuspended"
                        checked={korisniciStanje === "nonSuspended"}
                        onChange={handlekorisniciStanjeChange}
                    />
                    Nesuspendirani
                </label>
            </div>

            <div className="row">
                {error && <p style={{ color: "red" }}>Greška: {error}</p>}
                {prikaz.length > 0 ? (
                    prikaz.map((korisnik) => (
                        <div key={korisnik.kupacId || korisnik.trgovinaId || korisnik.moderatorId} className="col-lg-4 col-md-6 col-12 mb-3">
                            <div className="card">
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
                                    
                                    {korisniciStanje === "nonSuspended" && (
                                        <button
                                        className="add-to-cart-btn-no"
                                        onClick={() => suspendirajKorisnika(korisnik,korisniciTip)}
                                        >
                                        Suspendiraj {korisniciTip =="kupac" && "kupca"} {korisniciTip =="trgovina" && "trgovinu"} {korisniciTip =="moderator" && "moderatora"}
                                        </button>
                                    )}
                                    {korisniciStanje === "suspended" && (
                                        <button
                                        className
                                        onClick={() => odsuspendirajKorisnika(korisnik,korisniciTip)}
                                        >
                                        Odsuspendiraj {korisniciTip =="kupac" && "kupca"} {korisniciTip =="trgovina" && "trgovinu"} {korisniciTip =="moderator" && "moderatora"}
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
    );
}
