/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2018 Neil C Smith.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 * 
 * 
 * Please visit https://www.praxislive.org if you need additional information or
 * have any questions.
 */
package org.praxislive.core;

/**
 *
 * @author Neil C Smith
 */
public final class Call extends Packet {

    /**
     *
     */
    public static enum Type {

        /**
         * Invoke control - response is mandatory.
         */
        INVOKE,
        /**
         * Invoke call - response is not required except in case of error.
         */
        INVOKE_QUIET,
        /**
         * Return call.
         */
        RETURN,
        /**
         * Error call.
         */
        ERROR
    };

    /**
     * Create a Call with Type INVOKE and empty arguments.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @return Call
     */
    public static Call createCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode) {

        return createCall(toAddress, fromAddress, timeCode, CallArguments.EMPTY, Type.INVOKE);
    }

    /**
     * Create a Call with Type INVOKE.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @param arg single Value which will be automatically wrapped in a
     * CallArguments object.
     * @return Call
     */
    public static Call createCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            Value arg) {
        return createCall(toAddress, fromAddress, timeCode, CallArguments.create(arg), Type.INVOKE);
    }

    /**
     * Create a Call with Type INVOKE.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @param args CallArguments (use CallArguments.EMPTY for no arguments)
     * @return Call
     */
    public static Call createCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            CallArguments args) {
        return createCall(toAddress, fromAddress, timeCode, args, Type.INVOKE);
    }

    /**
     * Create a Call with Type INVOKE_QUIET and empty empty arguments. This indicates
     * that the sender does not require a response (though it might still get one),
     * except in case of error.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @return Call
     */
    public static Call createQuietCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode) {
        return createCall(toAddress, fromAddress, timeCode, CallArguments.EMPTY, Type.INVOKE_QUIET);
    }
    
    /**
     * Create a Call with Type INVOKE_QUIET. This indicates that the sender does
     * not require a response (though it might still get one), except in case of
     * error.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @param arg single Value which will be automatically wrapped in a
     * CallArguments object.
     * @return Call
     */
    public static Call createQuietCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            Value arg) {
        return createCall(toAddress, fromAddress, timeCode, CallArguments.create(arg), Type.INVOKE_QUIET);
    }

    /**
     * Create a Call with Type INVOKE_QUIET. This indicates that the sender does
     * not require a response (though it might still get one), except in case of
     * error.
     *
     * @param toAddress ControlAddress of receiving Control.
     * @param fromAddress ControlAddress for response.
     * @param timeCode long nanosecond time relative to hub clock
     * @param args CallArguments
     * @return Call
     */
    public static Call createQuietCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            CallArguments args) {
        return createCall(toAddress, fromAddress, timeCode, args, Type.INVOKE_QUIET);
    }

    private static Call createCall(
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            CallArguments args,
            Call.Type type) {
        if (toAddress == null || fromAddress == null) {
            throw new NullPointerException();
        }
        if (args == null) {
            args = CallArguments.EMPTY;
        }
        String root = toAddress.getComponentAddress().getRootID();
        return new Call(root, toAddress, fromAddress, timeCode, args, type);
    }

    /**
     * Create a Call of type RETURN. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @return Call
     */
    public static Call createReturnCall(
            Call inwardCall) {
        return createResponseCall(inwardCall, CallArguments.EMPTY, Type.RETURN);
    }
    
    /**
     * Create a Call of type RETURN. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @param arg Value, will automatically be wrapped in a CallArguments
     * object.
     * @return Call
     */
    public static Call createReturnCall(
            Call inwardCall,
            Value arg) {
        return createResponseCall(inwardCall, CallArguments.create(arg), Type.RETURN);
    }
    
    /**
     * Create a Call of type RETURN. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @param args Arguments
     * @return Call
     */
    public static Call createReturnCall(
            Call inwardCall,
            CallArguments args) {
        return createResponseCall(inwardCall, args, Type.RETURN);
    }

    /**
     * Create a Call of type ERROR. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @return Call
     */
    public static Call createErrorCall(
            Call inwardCall) {
        return createResponseCall(inwardCall, CallArguments.EMPTY, Type.ERROR);
    }
    
    /**
     * Create a Call of type ERROR. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @param arg Value, will automatically be wrapped in a CallArguments
     * object.
     * @return Call
     */
    public static Call createErrorCall(
            Call inwardCall,
            Value arg) {
        return createResponseCall(inwardCall, CallArguments.create(arg), Type.ERROR);
    }
    
    /**
     * Create a Call of type ERROR. Addresses, ID and timeCode will
     * automatically be set from the given inward Call.
     *
     * @param inwardCall Call this is a response to.
     * @param args Arguments
     * @return Call
     */
    public static Call createErrorCall(
            Call inwardCall,
            CallArguments args) {
        return createResponseCall(inwardCall, args, Type.ERROR);
    }

    private static Call createResponseCall(
            Call inwardCall,
            CallArguments args,
            Call.Type type) {
        if (inwardCall == null) {
            throw new NullPointerException();
        }
        if (args == null) {
            args = CallArguments.EMPTY;
        }
        ControlAddress toAddress = inwardCall.getFromAddress();
        ControlAddress fromAddress = inwardCall.getToAddress();
        String root = toAddress.getComponentAddress().getComponentID(0);
        long timeCode = inwardCall.getTimecode();
        int matchID = inwardCall.getID();
        return new Call(root, toAddress, fromAddress, timeCode, args, type, matchID);
    }

    private final Type type;
    private final CallArguments args;
    private final ControlAddress toAddress;
    private final ControlAddress fromAddress;
    private final int matchID;

    private Call(
            String root,
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            CallArguments args,
            Type type,
            int matchID) {
        super(root, timeCode);
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.args = args;
        this.type = type;
        this.matchID = matchID;
    }

    private Call(
            String root,
            ControlAddress toAddress,
            ControlAddress fromAddress,
            long timeCode,
            CallArguments args,
            Type type) {
        super(root, timeCode);
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.args = args;
        this.type = type;
        this.matchID = getID();
    }

    /**
     * Get the type of this call, either INVOKE, INVOKE_QUIET, RETURN or ERROR
     *
     * @return Call.Type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Get the Arguments of this Call.
     *
     * @return CallArguments
     */
    public CallArguments getArgs() {
        return this.args;
    }

    /**
     * Get the ControlAddress that this Call should be sent to.
     *
     * @return ControlAddress
     */
    public ControlAddress getToAddress() {
        return this.toAddress;
    }

    /**
     * Get the ControlAddress that this Call is being sent from, and if of type
     * INVOKE or INVOKE_QUIET, where RETURN and ERROR calls should be sent.
     *
     * @return
     */
    public ControlAddress getFromAddress() {
        return this.fromAddress;
    }

    /**
     * ID to match up calls and responses.
     *
     * For INVOKE and INVOKE_QUIET calls, this will return the same as getID().
     * For RETURN and ERROR calls, this ID will match the ID of the invoking
     * call.
     *
     * @return long ID
     */
    public int getMatchID() {
        return this.matchID;
    }

    /**
     * String representation of this Call. Only to be used for debugging
     * purposes. It is not guaranteed to retain the same format.
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Call " + super.toString());
        sb.append("\nTo : " + toAddress);
        sb.append("\nFrom : " + fromAddress);
        sb.append("\nType : " + type);
        sb.append("\nID : " + getID());
        sb.append("\nMatch ID : " + matchID);
        sb.append("\nArguments {");
        int count = args.getSize();
        if (count > 0) {
            for (int i = 0; i < args.getSize(); i++) {
                sb.append("\n    " + args.get(i));
            }
            sb.append("\n}");
        } else {
            sb.append("}\n");
        }

        return sb.toString();

    }
}
