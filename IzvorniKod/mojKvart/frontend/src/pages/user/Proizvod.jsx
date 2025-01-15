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

  const povecajKolicinu = () => {
    setKolicina(kolicina + 1);
  };


  const smanjiKolicinu = () => {
    if (kolicina > 1) {
      setKolicina(kolicina - 1); 
    }
  };

  
  useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
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
                return response.text().then(text => {throw new Error(text)});
            }
            return response.json();
        })
        .then(data => {
            setEmail(data.email);
        })
        .catch(error => setError(error.message));
}, []);


useEffect(() => {
  const token = localStorage.getItem('token');
  const options = {
      method: 'GET',
      headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      },
  };

  if (email) {
      fetch(`/api/kupacs/${email}`, options)
            .then(response => {
               
               if (!response.ok) {
                  return response.text().then(text => {throw new Error(text)});
               }
               return response.json();
            })
            .then(data => {
               setKupacId(data.kupacId);
            })
            .catch(error => setError(error.message));
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
      console.log("Product data:", data);
      setProizvod(data);
      // Ensure that trgovinaId is valid before fetching store name
      if (data.trgovina) {
        fetchTrgovinaName(data.trgovina, token); // Pass the token to the fetchTrgovinaName function
      } else {
        setError("Store ID is undefined or invalid.");
      }
    })
    .catch((err) => {
      setError(err.message);
    });

  // Fetch the product's average rating
  fetch(`/api/ocjenaProizvodKupacs/ocjena/${proizvodId}`, options)
    .then((response) => {
      if (!response.ok) {
        return response.text().then((text) => {
          throw new Error(`Greška pri dohvaćanju ocjene: ${response.status} - ${text}`);
        });
      }
      return response.json();
    })
    .then((data) => {
      if (data) {
        setSrednjaOcjena(data);
      }
    })
    .catch((err) => {
      setSrednjaOcjena(null);
    });
}, [proizvodId]);

// Function to fetch the store name
const fetchTrgovinaName = async (trgovina, token) => {
  try {
    const options3 = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    };
    const response = await fetch(`/api/trgovinas/getById/${trgovina}`, options3);
    if (!response.ok) {
      throw new Error(`Failed to fetch store with id ${trgovina}`);
    }
    const name = await response.json();
    setTrgovinaNames(prevNames => ({ ...prevNames, [trgovina]: name.trgovinaNaziv }));
  } catch (error) {
    console.error(`Error fetching store name for id ${trgovina}:`, error);
    setError(error.message); // Set the error message if needed
  }
};

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
                  <p id = "trg">{trgovinaNames[proizvod.trgovina]} </p>
                  <p id = "kat"><span id="kats">Kategorija:</span> {proizvod.proizvodKategorija}</p>
                  <p id = "op">{proizvod.proizvodOpis} </p>
                  <p></p>
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
