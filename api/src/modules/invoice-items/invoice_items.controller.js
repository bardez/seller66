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
    if(req.params.invoice_id > 0){
        const result = await sequelize.query(`
          SELECT
              * 
          FROM
              invoice_items it
          INNER JOIN products p ON
            p.id = it.product_id
          WHERE
              it.invoice_id = :invoice_id
            group by p.id
        `,{
            replacements:{
              invoice_id: req.params.invoice_id
            },
            type: sequelize.QueryTypes.SELECT
        });
    

      if(result.length > 0){
          resultSuccess('invoice_items', res)(result);
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