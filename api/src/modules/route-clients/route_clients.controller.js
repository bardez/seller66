import { RouteClientsModel, ClientsModel, RoutesModel } from '../../models'
import jwt from 'jsonwebtoken'
import HTTP from '../../utils/http_headers'
import { paginatedResults } from '../../utils/paginatedResults';
import { sequelize } from '../../models'
import {
    resultSuccess,
    resultError,
    resultEmpty
} from '../../utils/response';
const fields = [
    'name'
]
const getAll = async (req, res) =>{

  const result = await sequelize.query(`
        SELECT
           c.*
        FROM
            route_clients rc
        INNER JOIN clients c ON
          c.id = rc.client_id
        WHERE
          rc.visited = 0
        GROUP BY c.id
    `,{
      type: sequelize.QueryTypes.SELECT
    });
    

    if(result.length > 0){
        resultSuccess('route', res)(result);
    }else{
        resultEmpty(res);
    }
}

const getById = async (req, res) =>{
    if(req.params.route_id > 0){
        const result = await sequelize.query(`
          SELECT
              c.* 
          FROM
              route_clients rc
          INNER JOIN clients c ON
            c.id = rc.client_id
          WHERE
              rc.route_id = :route_id AND rc.visited = 0
            group by c.id
        `,{
            replacements:{
              route_id: req.params.route_id
            },
            type: sequelize.QueryTypes.SELECT
        });
    

      if(result.length > 0){
          resultSuccess('route', res)(result);
      }else{
          resultEmpty(res);
      }
    }
    else{
        resultEmpty(res);
    }
}

export default{
    getAll,
    getById
}