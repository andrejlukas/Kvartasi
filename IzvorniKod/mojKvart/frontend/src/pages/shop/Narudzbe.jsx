import React, { useEffect, useState } from "react";
import { Navbar } from "../../components/ShopNavbar";
import '../../styles/ShopNarudzbe.css';

export function ShopNarudzbe() {
  const [shopEmail, setShopEmail] = useState(null);
  const [shopId, setShopId] = useState(null);
  const [error, setError] = useState("");
  const [nepreuzeteNarudzbe, setNepreuzeteNarudzbe] = useState([]);
  const [preuzeteNarudzbe, setPreuzeteNarudzbe] = useState([]);
  const [selectedNarudzbe, setSelectedNarudzbe] = useState([]);
  const [narudzbeType, setNarudzbeType] = useState("nepreuzete"); // "nepreuzete" ili "preuzete"

  useEffect(() => {
    const token = localStorage.getItem("token");
    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ oneLiner: token }),
    };

    fetch("/api/tokens/claims", options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setShopEmail(data.email);
      })
      .catch((error) => setError(error.message));
  }, []);

  useEffect(() => {
    if (!shopEmail) return;

    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };

    fetch(`/api/trgovinas/${shopEmail}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setShopId(data.trgovinaId);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, [shopEmail]);

  useEffect(() => {
    if (!shopId) return;

    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };

    // Fetch preuzete narudzbe
    fetch(`/api/racuns?trgovinaId=${shopId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        const preuzete = data.filter((narudzba) => narudzba.stanje === "P");
        setPreuzeteNarudzbe(preuzete);
        if (narudzbeType === "preuzete") {
          setSelectedNarudzbe(preuzete);
        }
      })
      .catch((error) => setError(error.message));

    // Fetch nepreuzete narudzbe
    fetch(`/api/kupacProizvods/narudzbeTrgovina/${shopId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {

        console.log(data);
        const nepreuzete = Object.entries(data).map(([racunId, proizvodi]) => ({
          racunId,
          kupacIme: proizvodi[0].kupacIme,
          kupacPrezime: proizvodi[0].kupacPrezime,
          proizvodi: proizvodi.map((p) => ({
            naziv: p.proizvodNaziv,
            cijena: p.proizvodCijena,
            kolicina: p.proizvodKolicina,
          })),
          ukupnaCijena: proizvodi.reduce((sum, p) => sum + p.proizvodCijena * p.proizvodKolicina, 0),
        }));

        setNepreuzeteNarudzbe(nepreuzete);
        if (narudzbeType === "nepreuzete") {
          setSelectedNarudzbe(nepreuzete);
        }
      })
      .catch((error) => setError(error.message));
  }, [shopId, narudzbeType]);

  const handleNarudzbeTypeChange = (type) => {
    setNarudzbeType(type);
    setSelectedNarudzbe(type === "nepreuzete" ? nepreuzeteNarudzbe : preuzeteNarudzbe);
  };

  const markAsPreuzeta = (racunId) => {
    const token = localStorage.getItem("token");
    const options = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ novoStanje: "P" }),
    };

    fetch(`/api/racuns/stanje/${racunId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
      })
      .then(() => {
        setNepreuzeteNarudzbe((prev) =>
          prev.filter((narudzba) => narudzba.racunId !== racunId)
        );
        setPreuzeteNarudzbe((prev) => [
          ...prev,
          ...nepreuzeteNarudzbe.filter((narudzba) => narudzba.racunId === racunId),
        ]);

        if (narudzbeType === "nepreuzete") {
          setSelectedNarudzbe((prev) =>
            prev.filter((narudzba) => narudzba.racunId !== racunId)
          );
        }
      })
      .catch((error) => setError(error.message));
  };

  return (
    <div>
      <Navbar />
      <div className="shop-orders-container">
        {error && <p className="shop-orders-error-text">{error}</p>}

        <div className="shop-orders-radio-buttons mb-3">
          <label className="me-3">
            <input
              type="radio"
              name="narudzbeType"
              value="nepreuzete"
              checked={narudzbeType === "nepreuzete"}
              onChange={() => handleNarudzbeTypeChange("nepreuzete")}
            />
            Nepreuzete narudžbe
          </label>
          <label>
            <input
              type="radio"
              name="narudzbeType"
              value="preuzete"
              checked={narudzbeType === "preuzete"}
              onChange={() => handleNarudzbeTypeChange("preuzete")}
            />
            Preuzete narudžbe
          </label>
        </div>

        <div>
        {selectedNarudzbe.map((narudzba) => (
  <div key={narudzba.racunId} className="shop-order-card my-3">
    <div className="shop-order-card-body">
      <h5 className="shop-order-card-title">Račun ID: {narudzba.racunId}</h5>
      {narudzbeType === "nepreuzete" ? (
        <>
          <p>Kupac: <strong>{narudzba.kupacIme} {narudzba.kupacPrezime}</strong></p>
          <ul>
            {narudzba.proizvodi.map((proizvod, index) => (
              <li key={index}>
                {proizvod.naziv} - {proizvod.kolicina} x {proizvod.cijena} €
              </li>
            ))}
          </ul>
          <p>Ukupna cijena: <strong>{narudzba.ukupnaCijena.toFixed(2)} € </strong></p>
          <button
            className="shop-order-btn shop-order-btn-success"
            onClick={() => markAsPreuzeta(narudzba.racunId)}
          >
            Označi kao preuzeto
          </button>
        </>
      ) : (
        <p>Status: Preuzeta</p>
      )}
    </div>
  </div>
))}
        </div>
      </div>
    </div>
  );
}
