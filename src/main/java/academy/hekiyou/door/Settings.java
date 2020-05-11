package academy.hekiyou.door;

import org.jetbrains.annotations.NotNull;

/**
 * A simple way for allowing developers to change error notices.
 * <p>
 * See {@link Settings.Builder} for a way of building a {@link Settings} instance.
 */
public final class Settings {
    
    /**
     * Represents the prefix for errors. Useful for when you're utilizing a character-controlled coloring system.
     *
     * @implSpec Default is: ""
     */
    private String errorPrefix;
    
    /**
     * Represents the prefix for the actual error. "Actual error" meaning the location in which the error occurred
     * in a command.
     * <p>
     * For instance, if the command given is "/tp x y z", where "x", "y", and "z" are integers,
     * executing "/tp 5 2 a", the "a" would be prefixed by actualErrorPrefix, while the rest
     * would be prefixed first by errorPrefix.
     * <p>
     * Using default values, this would be "/tp 5 2 -->a".
     *
     * @implSpec Default is: "-->"
     */
    private String invalidArgumentPrefix;
    
    /**
     * Represents the message format that is sent when a command errors during argument processing by bad usage.
     *
     * @implSpec Default is: "Usage: %s"
     */
    private String usageErrorFormat;
    
    /**
     * Represents the message that is sent in case of a permission error.
     *
     * @implSpec Default is: "Permission required not granted."
     */
    private String permissionError;
    
    /**
     * Represents the message that is sent in the event the user uses an invalid subcommand.
     *
     * @implSpec Default is: "Invalid subcommand. Subcommands are: %s"
     */
    private String invalidSubcommandError;
    
    private Settings(@NotNull String errorPrefix, @NotNull String invalidArgumentPrefix,
                     @NotNull String usageErrorFormat, @NotNull String permissionError,
                     @NotNull String invalidSubcommandError){
        this.errorPrefix = errorPrefix;
        this.invalidArgumentPrefix = invalidArgumentPrefix;
        this.usageErrorFormat = usageErrorFormat;
        this.permissionError = permissionError;
        this.invalidSubcommandError = invalidSubcommandError;
    }
    
    /**
     * Return the prefix for when there is an error
     *
     * @return The error prefix
     */
    @NotNull
    public String getErrorPrefix(){
        return errorPrefix;
    }
    
    /**
     * Return the prefix used to prefix the argument that is invalid
     *
     * @return The invalid argument prefix
     */
    @NotNull
    public String getInvalidArgumentPrefix(){
        return invalidArgumentPrefix;
    }
    
    /**
     * Return the message format to display when there is a usage error
     *
     * @return The usage string format
     *
     * @implSpec The first argument, by default, will be the command usage that is formatted with
     * {@link Settings#invalidArgumentPrefix} and {@link Settings#errorPrefix}
     */
    @NotNull
    public String getUsageErrorFormat(){
        return usageErrorFormat;
    }
    
    /**
     * Return the message to display when there is a permission error
     *
     * @return The permission error message
     *
     * @implSpec If a format string specifier ("%s") is present, it will formatted with the required permission node
     * @implSpec By default, the required permission node is not formatted into the final message
     */
    @NotNull
    public String getPermissionError(){
        return permissionError;
    }
    
    /**
     * Return the message to display when the given subcommand was invalid
     *
     * @return The invalid subcommand error message
     *
     * @implSpec If a format string specifier ("%s") is present, it will formatted with a list of subcommands
     * @implSpec By default, all available subcommands are formatted into the final message
     */
    @NotNull
    public String getInvalidSubcommandError(){
        return invalidSubcommandError;
    }
    
    /**
     * A builder class used to build a {@link Settings} object
     */
    public static class Builder {
        
        private String errorPrefix = "";
        private String invalidArgumentPrefix = "-->";
        private String usageErrorFormat = "Usage: %s";
        private String permissionError = "Permission required not granted.";
        private String invalidSubcommandError = "Invalid subcommand. Subcommands are: %s";
        
        /**
         * Sets the error prefix
         *
         * @param errorPrefix the new prefix to set
         *
         * @return The current builder
         *
         * @see Settings#errorPrefix
         */
        @NotNull
        public Builder errorPrefix(@NotNull String errorPrefix){
            this.errorPrefix = errorPrefix;
            return this;
        }
        
        /**
         * Sets the invalid argument prefix
         *
         * @param invalidArgumentPrefix the new prefix to set
         *
         * @return The current builder
         *
         * @see Settings#invalidArgumentPrefix
         */
        @NotNull
        public Builder invalidArgumentPrefix(@NotNull String invalidArgumentPrefix){
            this.invalidArgumentPrefix = invalidArgumentPrefix;
            return this;
        }
        
        /**
         * Sets the usage error format
         *
         * @param usageErrorFormat the new format to set
         *
         * @return The current builder
         *
         * @see Settings#usageErrorFormat
         */
        @NotNull
        public Builder usageErrorFormat(@NotNull String usageErrorFormat){
            this.usageErrorFormat = usageErrorFormat;
            return this;
        }
        
        /**
         * Sets the permission error message
         *
         * @param permissionError the new error to set
         *
         * @return The current builder
         *
         * @see Settings#permissionError
         */
        @NotNull
        public Builder permissionError(@NotNull String permissionError){
            this.permissionError = permissionError;
            return this;
        }
        
        /**
         * Sets the invalid subcommand message
         *
         * @param invalidSubcommandError the new error to set
         *
         * @return The current builder
         *
         * @see Settings#invalidSubcommandError
         */
        @NotNull
        public Builder invalidSubcommandError(@NotNull String invalidSubcommandError){
            this.invalidSubcommandError = invalidSubcommandError;
            return this;
        }
        
        @NotNull
        public Settings build(){
            return new Settings(errorPrefix, invalidArgumentPrefix, usageErrorFormat, permissionError, invalidSubcommandError);
        }
        
    }
    
}
