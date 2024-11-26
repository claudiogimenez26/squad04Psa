"use client";

import { RecursoActualContext } from "@/_context/recursoActualContext";
import { Label, Select } from "flowbite-react";
import { useContext } from "react";

export default function SelectorRecurso() {
  const recursoActualContext = useContext(RecursoActualContext);

  if (
    !recursoActualContext ||
    !recursoActualContext.state ||
    !recursoActualContext.state.recursos ||
    !recursoActualContext.state.recursoActual
  ) {
    return null;
  }

  const { recursos, recursoActual } = recursoActualContext.state;
  const dispatch = recursoActualContext.dispatch;

  function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
    const recursoSeleccionado = recursos.find((r) => r.id === e.target.value);
    if (recursoSeleccionado) {
      dispatch({ payload: recursoSeleccionado });
    }
  }

  return (
    <div className="space-y-2">
      <Label htmlFor="recursoId" value="Seleccione un recurso" />
      <Select
        id="recursoId"
        name="recursoId"
        value={recursoActual.id}
        onChange={handleChange}
      >
        {recursos.map((r) => (
          <option key={r.id} value={r.id}>
            {r.dni} - {r.nombre} {r.apellido}
          </option>
        ))}
      </Select>
    </div>
  );
}
