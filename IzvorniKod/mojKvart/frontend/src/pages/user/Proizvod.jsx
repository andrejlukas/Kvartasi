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
  const [notification, setNotification] = useState(null);

  const [trgovinaId, setTrgovinaId] = useState(null);
  const [racunId, setRacunId] = useState(null);
  const [kupacProizvodId, setKupacProizvodId] = useState(null);

  const [message,setMessage] = useState("")

  function zatvori() {
    setMessage("")
}

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
      //console.log(kupacId)
      //console.log(trgovinaId)
      //console.log(proizvodId)

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
    .then(response =>{
      if (!response.ok) {
        return response.text().then((text) => {
          throw new Error(text);
        });
      }
    })
    .then(updated => {
    setMessage("Proizvod dodan u kosaricu")
      
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



  const renderInteractiveStars = (currentRating, setRating) => {
    const totalStars = 5;
  
    const handleStarClick = (rating) => {
      setRating(rating); // Set the selected rating
    };
  
    return (
      <div>
        {Array.from({ length: totalStars }, (_, index) => (
          <span
            key={index}
            className={`interactive-stars ${index < currentRating ? "full-review-stars" : "empty-review-stars"}`}
            onClick={() => handleStarClick(index + 1)} // Set rating on star click
            style={{ cursor: "pointer", fontSize: "24px", marginRight: "5px" }}
          >
            {index < currentRating ? "★" : "☆"}
          </span>
        ))}
      </div>
    );
  };
  
  function submitRating(userOcjena) {
    if (!userOcjena || !proizvodId || !kupacId) {
      setError("Nedostaju informacije");
      return;
    }
  
    const token = localStorage.getItem("token");
  
    if (!token || !kupacId) {
      setError("Podaci za recenziju nisu dostupni");
      return;
    }
  
    const options4 = {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        ocjena: userOcjena,
        kupac: kupacId,
        proizvod: proizvodId,
      }),
    };
  
    setIsSubmitting(true); 
    fetch(`/api/ocjenaProizvodKupacs`, options4)
      .then((response) => {
        if (response.ok) {
          setNotification("Vaša ocjena je uspješno poslana!");
          setUserOcjena(null); 
          return response.json();
        } else {
          throw new Error("Failed to save changes");
        }
      })
      .catch((error) => {
        console.error("Error updating data:", error);
        setNotification("Došlo je do pogreške pri slanju ocjene."); 
      })
      .finally(() => {
        setIsSubmitting(false); 
        setTimeout(() => setNotification(null), 5000);
      });
  }
  
  
  
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
                <h2 id="naslovvv">{proizvod.proizvodNaziv}</h2>
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
                <div id="ocjen">
                <p id="ocjj">Ocijenite proizvod:</p>
                <div id="zvjez">{renderInteractiveStars(userOcjena, setUserOcjena)} {/* Show the stars */}
                </div>
                <button 
                  id="submit-ratingg"
                  onClick={() => submitRating(userOcjena)} // Submit the rating when the button is clicked
                  disabled={isSubmitting || !userOcjena} // Disable the button if submitting or no rating selected
                >
                  {isSubmitting ? "Slanje..." : "Pošaljite ocjenu"} {/* Display loading or submit text */}
                </button>
                {notification && (
                <div className="notification1">
                  {notification}
                </div>
              )}
              </div>
              </div>
            </div>
          )}
        </div>
      </div>
      {
                        message  && <div className="dodan-u-kosaricu-zatvori-div"><p>{message}</p> <button className="zatvaranje-obavijesti" onClick={() => zatvori()}>Zatvori</button></div>
                    }
    </div>
  );
}