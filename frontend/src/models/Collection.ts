export type CollectionModel = CollectionAlbumModel[]

export interface CollectionAlbumModel {
  id: number
  name: string
  idSpotify: string
  artistName: string
  imageUrl: string
  value: number
  user: User
}

export interface User {
  id: number
  email: string
  password: string
}
