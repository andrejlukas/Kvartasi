import React, { useEffect, useState } from "react";
import { Navbar } from "../../components/ModeratorNavbar";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../styles/UpravljanjeKupcima.css";

export function ModeratorUpravljanjeTrgovinama() {
  const [TrgovinaType, setTrgovinaType] = useState("nonSuspended");
  const [error, setError] = useState("");
  const [suspendedTrgovinas, setSuspendedTrgovinas] = useState([]);
  const [nonSuspendedTrgovinas, setNonSuspendedTrgovinas] = useState([]);
  const [trgovinas, setTrgovinas] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const options = {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    };
  
    // Dohvati suspendirane trgovine
    fetch("/api/trgovinas/suspended", options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setSuspendedTrgovinas(data)
        if (TrgovinaType === "suspended") {
            setTrgovinas(data);
          }
      })
      .catch((error) => {
        setError(error.message);
      });
  
    // Dohvati nesuspendirane trgovine
    fetch("/api/trgovinas", options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        const filteredNonSuspended = data.filter(
          (trgovina) => trgovina.trgovinaStatus !== "S"
        );
        setNonSuspendedTrgovinas(filteredNonSuspended);
        if (TrgovinaType === "nonSuspended") {
          setTrgovinas(filteredNonSuspended);
        }
      })
      .catch((error) => {
        setError(error.message);
      });

    
  }, [TrgovinaType]); 
  
  const handleTrgovinasTypeChange = (e) => {
    const selectedType = e.target.value;
    setTrgovinaType(selectedType);
    if (selectedType === "nonSuspended") {
        setTrgovinas(nonSuspendedTrgovinas);
    } else {
        setTrgovinas(suspendedTrgovinas);
    }
  };

  function suspendTrgovina(trgovinaData) {
    const token = localStorage.getItem("token");
    const options = {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ ...trgovinaData, trgovinaStatus: "S" }),
    };

    fetch(`/api/trgovinas/${trgovinaData.trgovinaId}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
      })
      .then(() => {
        setNonSuspendedTrgovinas((prev) =>
          prev.filter((customer) => customer.trgovinaId !== trgovinaData.trgovinaId)
        );
        setSuspendedTrgovinas((prev) => [...prev, { ...trgovinaData, trgovinaStatus: "S" }]);
        if (TrgovinaType === "nonSuspended") {
          setTrgovinas((prev) =>
            prev.filter((customer) => customer.trgovinaId !== trgovinaData.trgovinaId)
          );
        }
      })
      .catch((error) => {
        setError(error.message);
      });
  }

  const getStatusText = (status) => {
    switch (status) {
      case "S":
        return "Suspendiran";
      case "V":
        return "Verificiran";
      case "N":
        return "Neverificiran";
      default:
        return "Nepoznat status";
    }
  };


  return (
    <div>
  <div id="vani2" style={{ minHeight: "100vh" }}>
    <Navbar />
    <div id="customers-alt1" className="customer-section-alt1">
      <div id="customerController1">
        <label>
          <input
            type="radio"
            name="trgovinasType"
            value="nonSuspended"
            checked={TrgovinaType === "nonSuspended"}
            onChange={handleTrgovinasTypeChange}
          />
          Nesuspendirane trgovine
        </label>
        <label>
          <input
            type="radio"
            name="trgovinasType"
            value="suspended"
            checked={TrgovinaType === "suspended"}
            onChange={handleTrgovinasTypeChange}
          />
          Suspendirane trgovine
        </label>
      </div>
      <div className="row1">
        {trgovinas.length > 0 ? (
          trgovinas.map((trgovina) => (
            <div
              id="customersbox1"
              key={trgovina.trgovinaId}
              className="col-lg-4 col-md-6 col-12 mb-3-alt"
            >
              <div className="card-alt customer-card-alt1">
                <div className="card-body-alt1">
                  <h5 className="card-title-alt1">Trgovina ID: {trgovina.trgovinaId}</h5>
                  <p className="card-text-alt1">
                    <strong>Ime:</strong> {trgovina.trgovinaNaziv}
                  </p>
                  {/* <p className="card-text-alt1">
                    <strong>Prezime:</strong> {trgovina.kupacPrezime}
                  </p> */}
                  <p className="card-text-alt1">
                    <strong>Email:</strong> {trgovina.trgovinaEmail}
                  </p>
                  <p className="card-text-alt1">
                    <strong>Status:</strong> {getStatusText(trgovina.trgovinaStatus)}
                  </p>
                  {TrgovinaType === "nonSuspended" && (
                    <button
                      className="add-to-cart-btn-no"
                      onClick={() => suspendTrgovina(trgovina) }
                    >
                      Suspendiraj trgovinu
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>Nema trgovina u ovoj kategoriji.</p>
        )}
      </div>
    </div>
  </div>
</div>

  );
}
