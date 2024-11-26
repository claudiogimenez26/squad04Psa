"use client";

import { RecursoContext } from "@/_context/recursoContext";
import { Label, Select } from "flowbite-react";
import { useContext } from "react";

export default function SelectRecurso() {
  const recursoContext = useContext(RecursoContext);

  if (
    !recursoContext ||
    !recursoContext.state ||
    !recursoContext.state.recursos ||
    !recursoContext.state.recursoActual
  ) {
    return null;
  }

  const { recursos, recursoActual } = recursoContext.state;
  const dispatch = recursoContext.dispatch;

  function handleChange(e: React.ChangeEvent<HTMLSelectElement>) {
    const recursoSeleccionado = recursos.find((r) => r.id === e.target.value);
    console.log(recursoSeleccionado);
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
