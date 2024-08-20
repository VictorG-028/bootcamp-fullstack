import { AlbumModel } from '@/models/Album';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { useAuth } from '@/hooks/UseAuth';
import { albumApi, userApi } from '@/services/apiService';
import React, { useEffect, useState } from 'react';
import Navbar from '@/components/Navbar/Navbar';
import { Search, Wallet, BadgeDollarSignIcon, Loader2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { DialogDescription } from '@radix-ui/react-dialog';
import Album from '@/components/Album';
import "./style.css";

export default function Home() {
  const [albums, setAlbums] = useState<AlbumModel[]>([]);
  const [search, setSearch] = useState<string>("Rock");
  const [buying, setBuying] = useState<boolean>(false);
  const { isAuthenticated, token, balance, updateBalance } = useAuth();
  const _navigate = useNavigate();

  useEffect(() => {
    if (!isAuthenticated) {
      _navigate('/');
    }

    searchByQuery();
  }, [search]);

  async function handleBuy(album: AlbumModel, balance: number | undefined) {

    if ((balance || balance == 0) && balance < album.value) {
      toast.error("Fundos insuficientes");
    }

    setBuying(true);

    const body = {
      name: album.name,
      idSpotify: album.id,
      artistName: album.artists[0].name,
      imageUrl: album.images[0].url,
      value: album.value,
    }

    await albumApi.post("/api/albums/sale", body)
      .then((resp) => {
        if (resp instanceof Error) {
          toast.error("Error: Compra não exeuctada");
        }

        toast.success("Compra efetuada com sucesso!");
      }).catch(() => {
        toast.error("Error: Compra não exeuctada.");
      });;

    await new Promise((resolve) => {
      setTimeout(resolve, 1000);
    });

    await userApi.get("/api/wallet")
      .then((resp) => {
        updateBalance(resp.data.balance);
      }).catch(() => {
        toast.error("Error: Não conseguiu verificar wallet");
      });

    setBuying(false);
  }

  function parseDate(date: string) {
    // expect "YYYY-MM-DD" and returns "DD-MM-YYYY"
    const parts = date.split("-");
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
  }

  function searchByQuery() {
    albumApi.defaults.headers.common.Authorization = `Basic ${token}`;
    albumApi.get(`/api/albums/all?searchText=${search}`)
      .then((resp) => {
        setAlbums(resp.data);
      });
  }

  return (
    <main className="min-h-screen text-black">
      <div style={{ backgroundPositionY: '-100px' }} className="bg-beach bg-cover bg-no-repeat min-w-screen h-[450px] z-0">

        <div className="absolute inset-0 bg-black opacity-50 h-[450px] z-0"></div>
        <div className="absolute bg-gradient-to-b from-transparent via-transparent to-[#19181F] h-[450px] w-full z-0"></div>

        <div className="absolute inset-0 z-1 h-[450px]">
          <Navbar isAuthenticated={isAuthenticated} routeName='/home'> </Navbar>
          <div className="w-full">
            <div className="mt-32 ml-[1.25rem] mb-2 w-[500px] text-white">
              <h1 className="text-[40px] leading-[1em] font-semibold">A história da música não pode ser esquecida!</h1>
              <p className="mt-4 text-2xl font-lato">Sucessos que marcaram o tempo!!!</p>
            </div>
          </div>
        </div>
      </div>

      <div className="flex flex-col items-center justify-between w-[100%] sm:min-w-screen min-h-screen bg-[#19181F]">

        <div className="mt-8 mb-0 w-80 justify-start flex flex-row border-[#CBCAD7] border-2 rounded-[12px]">
          <Input
            className="border-0 text-white"
            type="search"
            name="search"
            placeholder="search"
            value={search}
            onChange={(event) => { setSearch(event.target.value) }}
          />
          <Button title="search" variant="default" onClick={() => { }}>
            <Search className="stroke-[#FAFAFF] w-4 h-4" />
          </Button>
        </div>

        <section className="justify-center h-auto w-[90%]">
          {(search === "" || search === "Rock") && <h2 className="text-white font-bold text-[3rem] font-lato">Trends</h2>}
          <Carousel
            opts={{
              align: "start",
              loop: true,
              duration: 85,
              // slidesToScroll: undefined,
              // slides: "",
            }}
            className="w-full"
          >
            <CarouselContent className="">
              {albums?.map((album, i) => (
                <CarouselItem key={i} className="basis-1/1 sm:basis-1/2 md:basis-1/3 lg:basis-1/4 xl:basis-1/5 2xl:basis-1/6">
                  <Dialog>
                    <DialogTrigger asChild >
                      <div className="size-max min-h-[300px]">
                        <Album
                          name={album.name}
                          imageUrl={album.images[0].url}
                          spotifyUrl={album.externalUrls.externalUrls.spotify}
                          price={album.value}
                          isInsideCarousel={true}
                        >
                        </Album>
                      </div>
                    </DialogTrigger>
                    <DialogContent className="bg-white flex flex-row grow m-0 p-0 gap-0 border-0">
                      <div style={{ '--bg-fundo': `url(${album.images[0].url})` } as React.CSSProperties}
                        className="bg-[image:var(--bg-fundo)] bg-cover bg-no-repeat size-[250px] max-w-[250px] max-h-[250px] rounded-l-md"
                      ></div>
                      <DialogHeader className="flex flex-col items-center justify-between grow">
                        <div className="flex flex-col items-center justify-start">
                          <DialogTitle className="mt-8 font-bold text-2xl font-lato max-w-[250px] text-nowrap overflow-hidden overflow-ellipsis">{album.name}</DialogTitle>
                          <p>{parseDate(album.releaseDate)}</p>
                        </div>
                        <DialogDescription className="font-normal text-xl w-[250px]">
                          <div className="flex flex-row justify-center gap-2">
                            <div className="text-white text-lg bg-slate-800 rounded-2xl w-[128px] h-[64px] flex flex-col justify-center items-center">
                              <Wallet></Wallet>
                              <p>R$ {balance}</p>
                            </div>
                            <div className="text-white text-lg bg-slate-800 rounded-2xl w-[128px] h-[64px] flex flex-col justify-center items-center">
                              <BadgeDollarSignIcon></BadgeDollarSignIcon>
                              <p>R$ {album.value}</p>
                            </div>
                          </div>
                        </DialogDescription>
                        {buying ?
                          <Button className="bg-[#FBBC05]" disabled>
                            <Loader2 className="h-12 animate-spin" />
                            Carregando...
                          </Button>
                          :
                          <Button className="bg-[#FBBC05] rounded-[40px] w-[150px] hover:bg-[#fbe205]"
                            onClick={() => handleBuy(album, balance)}>Comprar</Button>
                        }
                        <span className="h-0"></span>
                      </DialogHeader>
                    </DialogContent>
                  </Dialog>
                </CarouselItem>
              ))}
            </CarouselContent>
            <CarouselPrevious className="translate-y-[-50px]" />
            <CarouselNext className="translate-y-[-50px]" />
          </Carousel>
        </section>
        <section className="justify-end h-[400px]"></section>
      </div>
    </main>
  )
}
