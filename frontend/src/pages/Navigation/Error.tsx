import { ServerCrashIcon } from "lucide-react";
import { Link } from 'react-router-dom';

export function ErrorPage() {
  return (
    <div className="flex flex-col justify-center items-center text-center h-screen bg-gray-200">
      <h1 className="text-3xl font-bold text-red-600">Página de Erro</h1>
      <Link to={"/"} className="bg-slate-400 min-w-fit w-12 rounded-full">Início</Link>
      <ServerCrashIcon className="mt-8 size-20"></ServerCrashIcon>
    </div>
  );
}
