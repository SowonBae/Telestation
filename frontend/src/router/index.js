import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Room from '../views/Room.vue'
import Lobby from '../views/Lobby.vue'
import Spyfall from '../components/games/Spyfall.vue'
import Fakeartist from '../components/games/Fakeartist.vue'
import Telestation from '../components/games/Telestation.vue'



Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },  
  {
    path: '/room/:roomId',
    name: 'Room',
    component: Room,
    children : [
      {
        path: '/room/:roomId',
        name: 'Spyfall',
        component: Spyfall
      },
      {
        path: '/room/:roomId',
        name: 'Fakeartist',
        component: Fakeartist
      },
      {
        path: '/room/:roomId',
        name: 'Telestation',
        component: Telestation
      }
    ]
  },
  { 
    path: '/lobby',
    name: 'Lobby',
    component: Lobby
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
