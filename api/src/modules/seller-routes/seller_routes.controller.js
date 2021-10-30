import { SellerRoutesModel, ClientsModel, RoutesModel } from '../../models'
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
           r.id, r.name
        FROM
            seller_routes sr
        INNER JOIN routes r ON
          r.id = sr.route_id
        GROUP BY r.id
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
    if(req.params.seller_id > 0){
        const result = await sequelize.query(`
          SELECT
              r.id, r.name  
          FROM
              seller_routes sr
          INNER JOIN routes r ON
            r.id = sr.route_id
          WHERE
              sr.seller_id = :seller_id
            group by r.id
        `,{
            replacements:{
              seller_id: req.params.seller_id
            },
            type: sequelize.QueryTypes.SELECT
        });
    

    if(result.length > 0){
        resultSuccess('route', res)(result);
    }else{
        resultEmpty(res);
    }
    }
}

export default{
    getAll,
    getById
}