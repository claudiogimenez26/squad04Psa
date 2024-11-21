import Image from "next/image";

export default async function Home() {
  const data = await fetch(`${process.env.BACKEND_URL}/carga-horas`);
  const cargasHoras = await data.json();

  return <div>Hello World</div>;
}
