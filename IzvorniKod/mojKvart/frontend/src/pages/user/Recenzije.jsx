import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Navbar } from "../../components/Navbar";
import "../../styles/Recenzije.css";

export function Recenzije() {
    const { email } = useParams();
    const [recenzije, setRecenzije] = useState([]);
    const [error, setError] = useState(null);
    const [shop, setShop] = useState(null);
    const [trgovinaId, setTrgovinaId] = useState(null);
    const [emailKupac, setEmailKupac] = useState("");
    const [kupacId, setKupacId] = useState(null);
    
    useEffect(() => {
        const token = localStorage.getItem("token");
        const options = {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ oneLiner: token }),
        };
    
        fetch("/api/tokens/claims", options)
          .then((response) => {
            if (!response.ok) {
              return response.text().then((text) => {
                throw new Error(text);
              });
            }
            return response.json();
          })
          .then((data) => {
            setEmailKupac(data.email);
          })
          .catch((error) => setError(error.message));
      }, []);

      useEffect(() => {
          const token = localStorage.getItem("token");
          const options = {
            method: "GET",
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          };
      
          if (emailKupac) {
            fetch(`/api/kupacs/${emailKupac}`, options)
              .then((response) => {
                if (!response.ok) {
                  return response.text().then((text) => {
                    throw new Error(text);
                  });
                }
                return response.json();
              })
              .then((data) => {
                setKupacId(data.kupacId);
              })
              .catch((error) => setError(error.message));
          }
        }, [emailKupac]);

    // Dohvat trgovine na osnovu email-a
    useEffect(() => {
        const token = localStorage.getItem("token");
        const options = {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        };

        if (email) {
            fetch(`/api/trgovinas/${email}`, options)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Neuspješno dohvatanje trgovine.");
                    }
                    return response.json();
                })
                .then((data) => {
                    setShop(data);
                    setTrgovinaId(data.trgovinaId); // Postavljanje trgovinaId
                })
                .catch((error) => {
                    setError(error.message);
                });
        }
    }, [email]);

    // Dohvat odobrenih recenzija na osnovu trgovinaId
    useEffect(() => {
        const token = localStorage.getItem("token");
        const options = {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        };

        if (trgovinaId) {
            fetch(`/api/recenzijas/trgovinas/${trgovinaId}`, options)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error("Neuspješno dohvatanje recenzija.");
                    }
                    return response.json();
                })
                .then((data) => {
                    // Filtriranje odobrenih recenzija
                    const approvedRecenzije = data.filter(
                        (recenzija) => recenzija.odobrioModerator
                    );
                    setRecenzije(approvedRecenzije);
                })
                .catch((error) => {
                    setError(error.message);
                });
        }
    }, [trgovinaId]);

    // Funkcija za renderiranje zvjezdica
    const renderStars = (rating) => {
        const totalStars = 5;
        const fullStars = Math.floor(rating);
        const emptyStars = totalStars - fullStars;
    
        // Generiramo niz zvjezdica
        const stars = [
            ...Array(fullStars).fill("★"),
            ...Array(emptyStars).fill("☆")
        ];
    
        return stars.map((star, index) => (
            <span key={index} className={`review-star ${star === "★" ? "full-review-star" : "empty-review-star"}`}>
                {star}
            </span>
        ));
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0'); // Osigurava 2 znamenke
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Mjesec počinje od 0
        const year = date.getFullYear();
        return `${day}.${month}.${year}`; // Formatiraj kao DD.MM.YYYY
    };

   

    return (
        <div id="vanjskiii">
            <div>
                <Navbar />
            </div>
            <div id="vanjskic">
                <div id="listica">
                    <h1 id="naslovic">Recenzije za trgovinu {shop ? shop.trgovinaNaziv : ""}</h1>
                    {error && <p style={{ color: "red" }}>{error}</p>}
                    {recenzije.length > 0 ? (
                        <ul id="skupina">
                            {recenzije.map((recenzija) => (
                                <li id="tockica" key={recenzija.recenzijaId}>
                                    
                                    <p >
                                     {renderStars(recenzija.recenzijaZvjezdice)} {formatDate(recenzija.vrijemeKreiranja)}
                                    </p>
                                    <p id="rec">
                                       <strong> {recenzija.recenzijaOpis} </strong>
                                    </p>
                                    <p>
                                        <strong id="odg">Odgovor trgovine:</strong> {recenzija.recenzijaOdgovor || "Nema odgovora"}
                                    </p>
                                    
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p id="dojava">Trenutno nema odobrenih recenzija za ovu trgovinu.</p>
                    )}
                </div>
            </div>
        </div>
    );
}
