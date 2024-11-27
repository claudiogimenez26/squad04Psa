export function encodearFecha(fecha: Date) {
  const fechaLocalDateString = fecha.toLocaleDateString("es-AR");
  return encodeURIComponent(fechaLocalDateString);
}

export function desencodearFecha(fecha: string) {
  const fechaLocaleDateString = decodeURIComponent(fecha);
  const [dia, mes, anio] = fechaLocaleDateString.split("/");
  return new Date(Number(anio), Number(mes) - 1, Number(dia));
}
