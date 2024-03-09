export interface Session {
  id: number,
  sessionCode: string,
  status: string,
  isInitiator: boolean
  // httpSessionId: string
}
