export interface WalletModel {
  id?: number
  balance?: number
  points?: number
  lastUpdate?: string
  users?: Users
}

export interface Users {
  id?: number
  name?: string
  email?: string
  password?: string
}
