import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import Navbar from '@/components/Navbar/Navbar';
import { useAuth } from '@/hooks/UseAuth';
import Info from '@/components/Info';
import { CircleDollarSignIcon, Medal, ReceiptIcon, MailIcon } from 'lucide-react';
import { userApi } from '@/services/apiService';

export default function MyWallet() {
  const { isAuthenticated, balance, points, lastUpdate, email, updateWallet, token } = useAuth();
  const _navigate = useNavigate();
  const warningMsg = "Erro ao carregar informação";

  useEffect(() => {
    if (!isAuthenticated) {
      _navigate('/');
    }

    userApi.defaults.headers.common.Authorization = `Basic ${token}`;
    userApi.get("/api/wallet").then((walletRes) => {
      if (walletRes instanceof Error) {
        toast.error("[1] Erro ao atualizar informações da carteira.");
      }

      updateWallet(walletRes.data);
    }).catch(() => {
      toast.error("[catch] Erro ao atualizar informações da carteira.");
    });
  }, []);

  function parseDateTime(dateTime: string) {

    const [date, time] = dateTime.split("T");
    const [year, month, day] = date.split("-");
    const [hour_min_sec, milisecs] = time.split(".");
    const [hour, min, secs] = hour_min_sec.split(":");

    return `${day}/${month}/${year}, às ${hour}:${min}`;
  }

  return (
    <main className="bg-[#19181F] h-screen min-w-fit text-white">
      <Navbar isAuthenticated={isAuthenticated} routeName='/myWallet'></Navbar>

      <h1 className="mt-20 ml-[5rem] font-Lato font-bold text-[3rem] line-height leading-[3.6rem]">Minha Carteira</h1>

      <section className="mt-8 ml-[4rem] flex flex-row gap-12">
        <Info icon={<CircleDollarSignIcon></CircleDollarSignIcon>} titleText="Saldo" contentText={balance !== undefined ? balance.toFixed(2).toString() : warningMsg} ></Info>
        <Info icon={<Medal></Medal>} titleText="Pontos" contentText={points !== undefined ? points.toFixed(2).toString() : warningMsg} ></Info>
        <Info icon={<ReceiptIcon></ReceiptIcon>} titleText="Ultima Modificação" contentText={lastUpdate ? parseDateTime(lastUpdate) : warningMsg} ></Info>
        <Info icon={<MailIcon></MailIcon>} titleText="Email" contentText={email ? email : warningMsg} ></Info>
      </section>

      <section className="mt-16 ml-[4rem]">
        TODO - Adicionar botões (adicionar fundos, modificar conta)
      </section>
    </main>
  );
};
function updateWalletModel(data: any) {
  throw new Error('Function not implemented.');
}

