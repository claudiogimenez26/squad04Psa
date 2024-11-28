export function encodearFecha(fecha: Date) {
  const dia = fecha.getDate().toString().padStart(2, "0");
  const mes = (fecha.getMonth() + 1).toString().padStart(2, "0");
  const anio = fecha.getFullYear();
  const fechaFormateada = `${dia}/${mes}/${anio}`;
  return encodeURIComponent(fechaFormateada);
}

export function desencodearFecha(fecha: string) {
  const fechaLocaleDateString = decodeURIComponent(fecha);
  const [dia, mes, anio] = fechaLocaleDateString.split("/");
  return new Date(Number(anio), Number(mes) - 1, Number(dia));
}
