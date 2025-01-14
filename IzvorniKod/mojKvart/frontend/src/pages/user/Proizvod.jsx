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

  const povecajKolicinu = () => {
    setKolicina(kolicina + 1);
  };


  const smanjiKolicinu = () => {
    if (kolicina > 1) {
      setKolicina(kolicina - 1); 
    }
  };
  

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

    // Dohvaćanje podataka o proizvodu
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
      })
      .catch((err) => {
        setError(err.message);
      });

      fetch(`/api/ocjenaProizvodKupacs/ocjena/${proizvodId}`, options)
      .then((response) => {
        console.log("Ocjena API odgovor:", response);  

        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(`Greška pri dohvaćanju ocjene: ${response.status} - ${text}`);
          });
        }

        return response.json();  
      })
      .then((data) => {
        console.log("Dohvaćena ocjena:", data);  
        if (data) {
          setSrednjaOcjena(data);
        }
      })
      .catch((err) => {
        console.error("Greška u dohvaćanju ocjene:", err);  
        setSrednjaOcjena(null);  
      });


      
  }, [proizvodId]);

  const renderStars = (srednjaOcjena) => {
    if (srednjaOcjena === null) {
        return (
          <div className="stars">
            <span className="star empty">☆</span>
            <span className="star empty">☆</span>
            <span className="star empty">☆</span>
            <span className="star empty">☆</span>
            <span className="star empty">☆</span>
          </div>
        );
      }

    if (typeof srednjaOcjena !== "number" || isNaN(srednjaOcjena)) {
      return <span>Ocjena nije dostupna</span>;
    }
  
    const totalStars = 5;
    const fullStars = Math.floor(srednjaOcjena);  
    const halfStars = srednjaOcjena % 1 >= 0.5 ? 1 : 0;  
    const emptyStars = totalStars - fullStars - halfStars;  
  
    let stars = [];

    for (let i = 0; i < fullStars; i++) {
      stars.push(<span key={`full-${i}`} className="star full">★</span>);
    }
    for (let i = 0; i < halfStars; i++) {
      stars.push(<span key={`half-${i}`} className="star half">★</span>);
    }
    for (let i = 0; i < emptyStars; i++) {
      stars.push(<span key={`empty-${i}`} className="star empty">★</span>);
    }
      return <div className="stars">{stars}</div>;
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
                  <p id = "trg">{proizvod.trgovina} </p>
                  <p id = "kat"><span id="kats">Kategorija:</span> {proizvod.proizvodKategorija}</p>
                  <p id = "op">{proizvod.proizvodOpis}</p>
                  <div>
                    {srednjaOcjena !== null ? (
                        <>
                        <div className="stars">
                            {renderStars(srednjaOcjena)}  <span id="un"> {srednjaOcjena.toFixed(2)} </span>
                        </div>
                        
                        </>
                    ) : (
                        <>
                        <div className="stars">
                            {renderStars(null)} 
                        </div>
                        <span>Proizvod još nije ocijenjen.</span>
                        </>
                    )}
                    </div>
                    <div id="gumbici">
                    <div id="cij">
                     {proizvod.proizvodCijena} €/kom
                    </div>
                    <div id="par">
                        <div id="kolicinaa">
                        <button id="minus" onClick={smanjiKolicinu}>-</button>
                        <span id="broj-komada">{kolicina}</span> 
                        <button id="plus" onClick={povecajKolicinu}>+</button>
                        </div>
                        <div>
                        <button id="dodaj-kosaricu">Dodaj u košaricu</button>
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
