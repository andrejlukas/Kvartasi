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
    const [novaRecenzija, setNovaRecenzija] = useState({ opis: "", zvjezdice: 0 });
    const [successMessage, setSuccessMessage] = useState("");

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

    const renderInteractiveStars = (currentRating, setRating) => {
        const totalStars = 5;
    
        return (
            <div>
                {Array.from({ length: totalStars }, (_, index) => (
                    <span
                        key={index}
                        className={`interactive-star ${index < currentRating ? "full-review-star" : "empty-review-star"}`}
                        onClick={() => setRating(index + 1)} // Postavlja ocjenu na kliknutu zvjezdicu
                        style={{ cursor: "pointer", fontSize: "24px", marginRight: "5px" }}
                    >
                        {index < currentRating ? "★" : "☆"}
                    </span>
                ))}
            </div>
        );
    };

    const handleAddRecenzija = (e) => {
        e.preventDefault();
        const token = localStorage.getItem("token");

        const options = {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                recenzijaOpis: novaRecenzija.opis,
                recenzijaZvjezdice: novaRecenzija.zvjezdice,
                kupacId,
                trgovinaId,
                vrijemeKreiranja: Date.now(),
                recenzijaOdgovor: null,
                odobrioModerator: false
            }),
        };

        fetch("/api/recenzijas", options)
        .then((response) => {
            console.log("Response Status:", response.status);
            return response.text(); // Get the response as text first
        })
        .then((text) => {
            console.log("Response Text:", text); // Log the full response
            try {
                const data = JSON.parse(text); // Try parsing as JSON
                setRecenzije((prev) => [...prev, data]);
                setSuccessMessage("Recenzija uspješno dodana!");
                setNovaRecenzija({ opis: "", zvjezdice: 0 });
            } catch (error) {
                console.error("Error parsing JSON:", error);
                setError("Neispravan odgovor s poslužitelja.");
            }
        })
        .catch((error) => {
            console.error("Request failed:", error);
            setError(error.message);
        });
    
    };


    return (
        <div id="vanjskiii">
            <div>
                <Navbar />
            </div>
            <div id="vanjskic">
                <div id="unosic">
                <h1 id="naslovic1">Dodajte novu recenziju za trgovinu {shop ? shop.trgovinaNaziv : ""}</h1>
                {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}
                <form onSubmit={handleAddRecenzija}>
                    <div className="review-rating">
                        <label id="ocjenaa">Vaša ocjena:</label>
                        {renderInteractiveStars(novaRecenzija.zvjezdice, (rating) =>
                            setNovaRecenzija({ ...novaRecenzija, zvjezdice: rating })
                        )}
                    </div>
                    <div className="review-description">
                        <label id="opiss">Opis recenzije:</label>
                        <textarea
                            id="kutijica"
                            value={novaRecenzija.opis}
                            onChange={(e) =>
                                setNovaRecenzija({ ...novaRecenzija, opis: e.target.value })
                            }
                            required
                        />
                    </div>
                    <button type="submit" className="submit-button">Dodaj recenziju</button>
                </form>

                </div>
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
