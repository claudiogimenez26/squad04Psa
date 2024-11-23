"use client";

import { Select } from "flowbite-react";
import { Recurso } from "@/app/lib/types";
import React, { useContext } from "react";
import { LoginContext } from "../context/LoginContext";

export default function ({ recursos }: { recursos: Recurso[] }) {
  const loginContext = useContext(LoginContext);

  if (!loginContext || !loginContext.state) {
    return <div></div>;
  }

  const { state, dispatch } = loginContext;

  function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
    const recursoSeleccionado = recursos.find((r) => r.id === e.target.value);
    if (recursoSeleccionado) {
      dispatch({ type: "ACTUALIZAR_RECURSO", payload: recursoSeleccionado });
    }
  }

  return (
    <Select id="recursos" value={state.id} onChange={handleChange}>
      {recursos.map((r) => (
        <option key={r.id} value={r.id}>
          {r.dni} - {r.nombre} {r.apellido}
        </option>
      ))}
    </Select>
  );
}
