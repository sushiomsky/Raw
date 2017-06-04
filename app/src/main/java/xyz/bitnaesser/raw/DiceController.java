package xyz.bitnaesser.raw;

import xyz.bitnaesser.raw.dice.client.web.BeginSessionResponse;
import xyz.bitnaesser.raw.dice.client.web.DiceWebAPI;

/**
 * Created by sushi on 03.06.17.
 */

public class DiceController {
    BeginSessionResponse beginSessionResponse;

    public DiceController(){
        beginSessionResponse = DiceWebAPI.BeginSession("26ae3c0fc1064a66982a9dfa21ec22b9");
    }

    public long getBalance(){
        return  DiceWebAPI.toSatoshis(beginSessionResponse.getSession().getBalance());
    }
}
