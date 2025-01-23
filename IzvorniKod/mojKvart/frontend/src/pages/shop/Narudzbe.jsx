import React, { useEffect, useState } from "react";
import { Navbar } from "../../components/ShopNavbar";
import '../../styles/ShopNarudzbe.css'

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

    fetch(`/api/racuns?trgovinaId=${shopId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        const nepreuzete = data.filter((narudzba) => narudzba.stanje === "T");
        const preuzete = data.filter((narudzba) => narudzba.stanje === "P");

        setNepreuzeteNarudzbe(nepreuzete);
        setPreuzeteNarudzbe(preuzete);

        // Postavi početni prikaz narudžbi na nepreuzete
        setSelectedNarudzbe(nepreuzete);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, [shopId]);

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
        // Ažuriraj lokalni state
        setNepreuzeteNarudzbe((prev) =>
          prev.filter((narudzba) => narudzba.racunId !== racunId)
        );
        setPreuzeteNarudzbe((prev) => [
          ...prev,
          ...nepreuzeteNarudzbe.filter((narudzba) => narudzba.racunId === racunId),
        ]);

        // Ažuriraj trenutno prikazane narudžbe
        if (narudzbeType === "nepreuzete") {
          setSelectedNarudzbe((prev) =>
            prev.filter((narudzba) => narudzba.racunId !== racunId)
          );
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  return (
   <div>
     <Navbar />
     <div className="shop-orders-container">
       {error && <p className="shop-orders-error-text">{error}</p>}
 
       <div className="shop-orders-radio-buttons mb-3">
         <div id="divov">
            <div>
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
         </div>
         <div>
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
         </div>
       </div>
   
      <div id="sve">
       {selectedNarudzbe.length > 0 ? (
         selectedNarudzbe.map((narudzba) => (
           <div key={narudzba.racunId} className="shop-order-card my-3">
             <div className="shop-order-card-body">
               <h5 className="shop-order-card-title">Račun ID: {narudzba.racunId}</h5>
               <p>Status: {narudzbeType === "nepreuzete" ? "Nepreuzeta narudžba" : "Preuzeta narudžba"}</p>
               {narudzbeType === "nepreuzete" && (
                 <button
                   className="shop-order-btn shop-order-btn-success"
                   onClick={() => markAsPreuzeta(narudzba.racunId)}
                 >
                   Označi kao preuzeto
                 </button>
               )}
             </div>
           </div>
         ))
       ) : (
         <p>
           Nema {narudzbeType === "nepreuzete" ? "nepreuzetih" : "preuzetih"} narudžbi.
         </p>
       )}
     </div>
     </div>
   </div>
 ); 
}
