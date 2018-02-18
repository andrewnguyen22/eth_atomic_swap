package contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes20;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class AtomicSwap extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b5b5b5b61093a806100216000396000f300606060405263ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166327f10ee081146100695780635a8f9b811461008f5780637ec27ba1146100b1578063eb8ae1ed146100d6578063f325cc91146100fc575b600080fd5b61008d6004356001606060020a031960243516600160a060020a0360443516610193565b005b341561009a57600080fd5b61008d6001606060020a0319600435166102dc565b005b34156100bc57600080fd5b61008d6004356001606060020a0319602435166104ba565b005b61008d6004356001606060020a031960243516600160a060020a0360443516610727565b005b341561010757600080fd5b61011c6001606060020a0319600435166108a2565b604051898152602081018990526001606060020a03198816604082015260608101879052600160a060020a038087166080830152851660a082015260c0810184905282151560e0820152610100810182600281111561017757fe5b60ff168152602001995050505050505050505060405180910390f35b8160005b6001606060020a03198216600090815260208190526040902060070154610100900460ff1660028111156101c757fe5b146101d157600080fd5b6001606060020a03198316600090815260208190526040902060018101859055428155600581018054600160a060020a0333811673ffffffffffffffffffffffffffffffffffffffff199283161790925560048301805492861692821692909217909155346006830155600280830180546c0100000000000000000000000088049316929092179091556007909101805461ff001916610100835b02179055507fd2ae53b489af667edae5247fc948af6bab36cb0ec684fb14cc114c95905f4f1982338534604051600160a060020a0394851681529290931660208301526001606060020a0319166040808301919091526060820192909252608001905180910390a15b5b50505050565b6001606060020a03198116600090815260208190526040902060018101549054829101421161030a57600080fd5b6001606060020a0319811660009081526020819052604090206007015460ff161561033457600080fd5b60025b6001606060020a03198316600090815260208190526040902060070154610100900460ff16600281111561036757fe5b14156103c5576001606060020a03198216600090815260208190526040908190206005810154600690910154600160a060020a039091169181156108fc02919051600060405180830381858888f1935050505015156103c557600080fd5b5b60015b6001606060020a03198316600090815260208190526040902060070154610100900460ff1660028111156103f957fe5b1415610457576001606060020a03198216600090815260208190526040908190206004810154600690910154600160a060020a039091169181156108fc02919051600060405180830381858888f19350505050151561045757600080fd5b5b6001606060020a0319821660009081526020819052604090819020600701805460ff191660011790557f3d2a04f53164bedf9a8a46353305d6b2d2261410406df3b41f99ce6489dc003c9042905190815260200160405180910390a15b5b5050565b80826001606060020a031982166003826000604051602001526040519081526020908101906040518083038160008661646e5a03f115156104fa57600080fd5b5050604051516c01000000000000000000000000026bffffffffffffffffffffffff191614151561052a57600080fd5b6001606060020a0319821660009081526020819052604090206001810154905401421061055657600080fd5b6001606060020a0319821660009081526020819052604090206007015460ff161561058057600080fd5b60025b6001606060020a03198416600090815260208190526040902060070154610100900460ff1660028111156105b357fe5b1415610611576001606060020a03198316600090815260208190526040908190206004810154600690910154600160a060020a039091169181156108fc02919051600060405180830381858888f19350505050151561061157600080fd5b5b60015b6001606060020a03198416600090815260208190526040902060070154610100900460ff16600281111561064557fe5b14156106a3576001606060020a03198316600090815260208190526040908190206005810154600690910154600160a060020a039091169181156108fc02919051600060405180830381858888f1935050505015156106a357600080fd5b5b6001606060020a0319831660009081526020819052604090819020600701805460ff191660011790557f82498456531a1065f689ba348ce20bda781238c424cf36748dd40bc282831e039042905190815260200160405180910390a16001606060020a0319831660009081526020819052604090206003018490555b5b50505050565b8160005b6001606060020a03198216600090815260208190526040902060070154610100900460ff16600281111561075b57fe5b1461076557600080fd5b6001606060020a031983166000908152602081905260409020600180820186905542825560028201805473ffffffffffffffffffffffffffffffffffffffff199081166c010000000000000000000000008804179091556005830180548216600160a060020a0387811691909117909155600484018054909216339091161790556007909101805461ff001916610100835b02179055506001606060020a0319831660009081526020819052604090819020346006820181905590547f8f52c15a8dda5af727677001f0ceb13df57f42198fd5cdde86a06aee80333a4b9290918791879187913391905195865260208601949094526001606060020a0319909216604080860191909152600160a060020a0391821660608601529116608084015260a083019190915260c0909101905180910390a15b5b50505050565b60006020819052908152604090208054600182015460028301546003840154600485015460058601546006870154600790970154959694956c01000000000000000000000000909402949293600160a060020a0392831693919092169160ff80821691610100900416895600a165627a7a7230582054264d7ff3736ef9a141f8a979792a740b1b87bfb3e34086317794d90875482e0029\n";

    protected AtomicSwap(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AtomicSwap(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<RefundedEventResponse> getRefundedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Refunded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RefundedEventResponse> responses = new ArrayList<RefundedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RefundedEventResponse typedResponse = new RefundedEventResponse();
            typedResponse._refundTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RefundedEventResponse> refundedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Refunded",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RefundedEventResponse>() {
            @Override
            public RefundedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RefundedEventResponse typedResponse = new RefundedEventResponse();
                typedResponse._refundTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<RedeemedEventResponse> getRedeemedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Redeemed",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<RedeemedEventResponse> responses = new ArrayList<RedeemedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            RedeemedEventResponse typedResponse = new RedeemedEventResponse();
            typedResponse._redeemTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<RedeemedEventResponse> redeemedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Redeemed",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, RedeemedEventResponse>() {
            @Override
            public RedeemedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                RedeemedEventResponse typedResponse = new RedeemedEventResponse();
                typedResponse._redeemTime = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ParticipatedEventResponse> getParticipatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Participated",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes20>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ParticipatedEventResponse> responses = new ArrayList<ParticipatedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ParticipatedEventResponse typedResponse = new ParticipatedEventResponse();
            typedResponse._initiator = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._participator = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._hashedSecret = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ParticipatedEventResponse> participatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Participated",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Bytes20>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ParticipatedEventResponse>() {
            @Override
            public ParticipatedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ParticipatedEventResponse typedResponse = new ParticipatedEventResponse();
                typedResponse._initiator = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._participator = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._hashedSecret = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public List<InitiatedEventResponse> getInitiatedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Initiated",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes20>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<InitiatedEventResponse> responses = new ArrayList<InitiatedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            InitiatedEventResponse typedResponse = new InitiatedEventResponse();
            typedResponse._initTimestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._refundTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._hashedSecret = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._participant = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._initiator = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse._funds = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<InitiatedEventResponse> initiatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Initiated",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes20>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, InitiatedEventResponse>() {
            @Override
            public InitiatedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                InitiatedEventResponse typedResponse = new InitiatedEventResponse();
                typedResponse._initTimestamp = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._refundTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._hashedSecret = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._participant = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._initiator = (String) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse._funds = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> participate(BigInteger _refundTime, byte[] _hashedSecret, String _initiator, BigInteger weiValue) {
        Function function = new Function(
                "participate",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_refundTime),
                        new org.web3j.abi.datatypes.generated.Bytes20(_hashedSecret),
                        new org.web3j.abi.datatypes.Address(_initiator)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> refund(byte[] _hashedSecret) {
        Function function = new Function(
                "refund",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes20(_hashedSecret)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> redeem(byte[] _secret, byte[] _hashedSecret) {
        Function function = new Function(
                "redeem",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_secret),
                        new org.web3j.abi.datatypes.generated.Bytes20(_hashedSecret)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> initiate(BigInteger _refundTime, byte[] _hashedSecret, String _participant, BigInteger weiValue) {
        Function function = new Function(
                "initiate",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_refundTime),
                        new org.web3j.abi.datatypes.generated.Bytes20(_hashedSecret),
                        new org.web3j.abi.datatypes.Address(_participant)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Tuple9<BigInteger, BigInteger, byte[], byte[], String, String, BigInteger, Boolean, BigInteger>> swaps(byte[] param0) {
        final Function function = new Function("swaps",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes20(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bytes20>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint8>() {}));
        return new RemoteCall<Tuple9<BigInteger, BigInteger, byte[], byte[], String, String, BigInteger, Boolean, BigInteger>>(
                new Callable<Tuple9<BigInteger, BigInteger, byte[], byte[], String, String, BigInteger, Boolean, BigInteger>>() {
                    @Override
                    public Tuple9<BigInteger, BigInteger, byte[], byte[], String, String, BigInteger, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple9<BigInteger, BigInteger, byte[], byte[], String, String, BigInteger, Boolean, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (byte[]) results.get(2).getValue(),
                                (byte[]) results.get(3).getValue(),
                                (String) results.get(4).getValue(),
                                (String) results.get(5).getValue(),
                                (BigInteger) results.get(6).getValue(),
                                (Boolean) results.get(7).getValue(),
                                (BigInteger) results.get(8).getValue());
                    }
                });
    }

    public static RemoteCall<AtomicSwap> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AtomicSwap.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<AtomicSwap> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AtomicSwap.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static AtomicSwap load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwap(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static AtomicSwap load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AtomicSwap(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class RefundedEventResponse {
        public BigInteger _refundTime;
    }

    public static class RedeemedEventResponse {
        public BigInteger _redeemTime;
    }

    public static class ParticipatedEventResponse {
        public String _initiator;

        public String _participator;

        public byte[] _hashedSecret;

        public BigInteger _value;
    }

    public static class InitiatedEventResponse {
        public BigInteger _initTimestamp;

        public BigInteger _refundTime;

        public byte[] _hashedSecret;

        public String _participant;

        public String _initiator;

        public BigInteger _funds;
    }
}