import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import Navbar from '@/components/Navbar/Navbar';
import { useAuth } from '@/hooks/UseAuth';
import Info from '@/components/Info';
import { Disc3Icon, CircleDollarSignIcon } from 'lucide-react';
import { albumApi } from '@/services/apiService';
import { CollectionModel } from '@/models/Collection';
import Album from '@/components/Album';

export default function MyDiscs() {
  const { isAuthenticated, token } = useAuth();
  const [albums, setAlbums] = useState<CollectionModel>([]);
  const _navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      _navigate('/');
    }

    albumApi.defaults.headers.common.Authorization = `Basic ${token}`;
    albumApi.get("/api/albums/my-collection").then((resp) => {
      setAlbums(resp.data);
    }).catch(() => {
      toast.error("Error: falha ao pegar albums comprados");
    });
  }, []);

  return (
    <main className="bg-[#19181F] min-h-screen min-w-fit text-white">
      <Navbar isAuthenticated={isAuthenticated} routeName='/myDiscs'></Navbar>

      <h1 className="mt-20 ml-[5rem] font-Lato font-bold text-[3rem] line-height leading-[3.6rem]">Meus Discos</h1>

      <section className="mt-8 ml-[4rem] flex flex-row gap-12">
        <Info icon={<Disc3Icon></Disc3Icon>} titleText="Total de Albums" contentText={albums.length.toString()} ></Info>
        <Info icon={<CircleDollarSignIcon></CircleDollarSignIcon>} titleText="Valor investido" contentText={albums?.reduce((acc, album) => acc + album.value, 0).toFixed(2).toString()} ></Info>
      </section>

      <section className="mt-8 ml-[3.1rem] grid grid-cols-2 sm:grid-cols-3 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-5 sm:gap-6 md:gap-8">
        {albums?.map((album, i) => (
          <div key={i}>
            <Album
              name={album.name}
              imageUrl={album.imageUrl}
              price={album.value}
              backgroundInPrice={true}
              animateOnHover={true}
            >
            </Album>
          </div>
        ))}
      </section>
    </main>
  );
};
