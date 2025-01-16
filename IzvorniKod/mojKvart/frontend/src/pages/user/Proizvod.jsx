import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Navbar } from "../../components/Navbar";
import "../../styles/Proizvod.css";

export function Proizvod() {
  const { proizvodId } = useParams();
  const [proizvod, setProizvod] = useState(null);
  const [srednjaOcjena, setSrednjaOcjena] = useState(null);
  const [error, setError] = useState(null);
  const [kolicina, setKolicina] = useState(1);
  const [trgovinaNames, setTrgovinaNames] = useState({});
  const [email, setEmail] = useState("");
  const [kupacId, setKupacId] = useState(null);
  const [userOcjena, setUserOcjena] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [trgovinaId, setTrgovinaId] = useState(null);
  const [racunId, setRacunId] = useState(null);
  const [kupacProizvodId, setKupacProizvodId] = useState(null);

  const povecajKolicinu = () => {
    setKolicina(kolicina + 1);
  };

  const smanjiKolicinu = () => {
    if (kolicina > 1) {
      setKolicina(kolicina - 1);
    }
  };

 
  

  const dodajUKosaricu = () => {

    const token = localStorage.getItem("token");
    if (!token) {
      setError("Nedostaje token za autorizaciju.");
      return;
    }
  
    // Provjerite jesu li potrebne varijable postavljene
    if (!kupacId || !trgovinaId || !proizvodId) {
      setError("Podaci za kupca, trgovinu ili proizvod nisu dostupni.");
      console.log(kupacId)
      console.log(trgovinaId)
      console.log(proizvodId)

      return;
    }
  


    const options = {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };
  
    
      fetch(`/api/kupacProizvods/dodaj/${kupacId}/${trgovinaId}/${proizvodId}/${kolicina}`, options)
    .then(response => response.ok && console.log("uspjela"))
    .then(updated => {
      console.log(updated)
    })
    .catch(error => {
        console.error('Error updating data:', error);
    })

  
    
  }

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
        setEmail(data.email);
      })
      .catch((error) => setError(error.message));
  }, []);

  useEffect(() => {
    const token = localStorage.getItem("token");
    console.log(token)
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };

    if (email) {
      fetch(`/api/kupacs/${email}`, options)
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
  }, [email]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setError("Nedostaje token za autorizaciju.");
      return;
    }

    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };

    fetch(`/api/proizvods/${proizvodId}`, options)
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(`Greška: ${response.status} - ${text}`);
          });
        }
        return response.json();
      })
      .then((data) => {
        setProizvod(data);
        if (data.trgovina) {
          setTrgovinaId(data.trgovina)
          fetchTrgovinaName(data.trgovina, token);
        } else {
          setError("Store ID is undefined or invalid.");
        }
      })
      .catch((err) => setError(err.message));

    fetch(`/api/ocjenaProizvodKupacs/ocjena/${proizvodId}`, options)
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(
              `Greška pri dohvaćanju ocjene: ${response.status} - ${text}`
            );
          });
        }
        return response.json();
      })
      .then((data) => {
        setSrednjaOcjena(data);
      })
      .catch(() => {
        setSrednjaOcjena(null);
      });
  }, [proizvodId]);

  const fetchTrgovinaName = async (trgovina, token) => {
    try {
      const options3 = {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      };
      const response = await fetch(`/api/trgovinas/getById/${trgovina}`, options3);
      if (!response.ok) {
        throw new Error(`Failed to fetch store with id ${trgovina}`);
      }
      const name = await response.json();
      setTrgovinaNames((prevNames) => ({
        ...prevNames,
        [trgovina]: name.trgovinaNaziv,
      }));
    } catch (error) {
      setError(error.message);
    }
  };



  const submitRating = async () => {
    const token = localStorage.getItem("token");
  
    if (!token) {
      setError("Nedostaje token za autentifikaciju. Prijavite se ponovo.");
      return;
    }
  
    if (!kupacId) {
      setError("Kupac ID nije pronađen. Molimo pokušajte ponovo.");
      return;
    }
  
    if (!userOcjena) {
      setError("Molimo odaberite ocjenu prije slanja.");
      return;
    }
  
    setIsSubmitting(true);
  
    // Generiramo jedinstveni ID ocjene
    const idOcjene = Date.now();
  
    // Pripremamo payload
    const payload = {
      id: idOcjene,
      proizvodId,
      kupacId,
      ocjena: userOcjena,
    };
  
    try {
      // Validacija tokena prije slanja zahtjeva (možete dodati endpoint za validaciju na serveru)
      const validateTokenResponse = await fetch("/api/tokens/validate", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token }),
      });
  
      if (!validateTokenResponse.ok) {
        const errorText = await validateTokenResponse.text();
        throw new Error(`Token nije valjan: ${errorText}`);
      }
  
      // Ako je token validan, nastavljamo sa slanjem ocjene
      const response = await fetch(`/api/ocjenaProizvodKupacs`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });
  
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Greška pri slanju ocjene: ${response.status} - ${errorText}`);
      }
  
      // Uspješna akcija
      setIsSubmitting(false);
      alert(`Vaša ocjena je uspješno zabilježena! ID ocjene: ${idOcjene}`);
      setUserOcjena(null); // Resetiramo ocjenu
    } catch (error) {
      // Obrada greške
      setIsSubmitting(false);
      setError(error.message);
    }
  };
  
  const renderStars = (srednjaOcjena) => {
    if (srednjaOcjena === null) {
      return (
        <div className="stars">
          {[...Array(5)].map((_, i) => (
            <span key={i} className="star empty">
              ☆
            </span>
          ))}
        </div>
      );
    }

    const totalStars = 5;
    const fullStars = Math.floor(srednjaOcjena);
    const halfStars = srednjaOcjena % 1 >= 0.5 ? 1 : 0;
    const emptyStars = totalStars - fullStars - halfStars;

    return (
      <div className="stars">
        {[...Array(fullStars)].map((_, i) => (
          <span key={`full-${i}`} className="star full">
            ★
          </span>
        ))}
        {[...Array(halfStars)].map((_, i) => (
          <span key={`half-${i}`} className="star half">
            ★
          </span>
        ))}
        {[...Array(emptyStars)].map((_, i) => (
          <span key={`empty-${i}`} className="star empty">
            ☆
          </span>
        ))}
      </div>
    );
  };

  return (
    <div id="prozorProizvod">
      <Navbar />
      <div id="prozorOut">
        <div id="infor">
          {error ? (
            <p style={{ color: "red" }}>{error}</p>
          ) : !proizvod ? (
            <p>Učitavanje podataka o proizvodu...</p>
          ) : (
            <div id="slika">
              <div id="slikica">
                <img
                  src={proizvod.proizvodSlika}
                  alt={proizvod.proizvodNaziv}
                  className="card-img-top-alt"
                />
              </div>
              <div id="pojedinosti">
                <h2 id="nas">{proizvod.proizvodNaziv}</h2>
                <p id="trg">{trgovinaNames[proizvod.trgovina]} </p>
                <p id="kat">
                  <span id="kats">Kategorija:</span> {proizvod.proizvodKategorija}
                </p>
                <p id="op">{proizvod.proizvodOpis} </p>
                <div>
                  {srednjaOcjena !== null ? (
                    <>
                      <div className="stars">
                        {renderStars(srednjaOcjena)}{" "}
                        <span id="un"> {srednjaOcjena.toFixed(2)} </span>
                      </div>
                    </>
                  ) : (
                    <>
                      <div className="stars">{renderStars(null)}</div>
                      <span>Proizvod još nije ocijenjen.</span>
                    </>
                  )}
                </div>
                <div>
                  <p>Ocijenite proizvod:</p>
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      className={`star-button ${userOcjena === star ? "selected" : ""}`}
                      onClick={() => setUserOcjena(star)}
                    >
                      {star} ★
                    </button>
                  ))}
                </div>
                <button
                  id="submit-rating"
                  onClick={submitRating}
                  disabled={isSubmitting}
                >
                  {isSubmitting ? "Slanje..." : "Pošalji ocjenu"}
                </button>
                <div id="gumbici">
                  <div id="cij">{proizvod.proizvodCijena} €/kom</div>
                  <div id="par">
                    <div id="kolicinaa">
                      <button id="minus" onClick={smanjiKolicinu}>
                        -
                      </button>
                      <span id="broj-komada">{kolicina}</span>
                      <button id="plus" onClick={povecajKolicinu}>
                        +
                      </button>
                    </div>
                    <div>
                      <button id="dodaj-kosaricu" onClick={dodajUKosaricu}>Dodaj u košaricu</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
